package com.hololive.livestream.Quartz;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;
import com.hololive.livestream.DAO.VideoDAO;
import com.hololive.livestream.DTO.VideoDTO;

/**
 * @author ngm95
 *
 * 예약된 영상들을 체크해서 상태가 변경되었는지 검사하는 QuartzJobBean
 * 매시 0분 0초부터 5분 간격으로 실행하므로 하루에 총 480번 실행됨
 * 
 * 실행 간격이 짧기 때문에 불필요한 API 요청을 줄이기 위해
 * 현재 시간이 예약된 시간을 넘은 영상들에 대해서만 검사를 실행함
 */
@Component
public class CheckUpcoming extends QuartzJobBean {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
		public void initialize(HttpRequest request) throws IOException {}
	}).setApplicationName("HoloLiveAutoScheduler").build();
	
	@Autowired
	private VideoDAO videoDao;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		StringBuilder log = new StringBuilder();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		log.append(format.format(Calendar.getInstance().getTime()) + " : checkUpcoming\n");
		
		// DB Upcoming 테이블에 있는 데이터를 리스트로 가져옴
		List<VideoDTO> upcomingList = videoDao.readAllInUpcoming();

		format = new SimpleDateFormat("yy.MM.dd HH:mm");
		
		// Upcoming 동영상의 예정 시간이 지났다면 Videos:list로 확인
		for (VideoDTO upcoming : upcomingList) {	
			if (upcoming.getScheduledStartTime().compareTo(format.format(Calendar.getInstance().getTime())) <= 0) {
				log.append(checkStart(upcoming, format));
			}
		}
		
		System.out.println(log);
	}

	public String checkStart(VideoDTO upcoming, DateFormat format) {
		StringBuilder log = new StringBuilder();
		
		log.append("\t member : " + upcoming.getMemberName() + "\n");
		try {
			YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
			videos.setFields("items(id, liveStreamingDetails/scheduledStartTime, liveStreamingDetails/actualStartTime)");
			videos.setKey(upcoming.getApiKey());     
			videos.setId(upcoming.getVideoId());
			videos.setMaxResults(1L); 

			videoDao.increaseQuotas1(upcoming.getApiKey());
			List<Video> videoList = videos.execute().getItems();

			if (videoList.isEmpty()) {						// 반환 결과가 통째로 null이면 예약이 취소된 경우이므로 Upcoming테이블에서 삭제
				log.append("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 취소됐습니다.\n");
				videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
				log.append("\t\t\t Upcoming 테이블에서 삭제\n");
			} else {										// actualStartTime!=null이면 Upcoming 테이블에서 삭제하고 Live 테이블에 저장
				VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
				if (liveStreaming.getActualStartTime() != null) {
					log.append("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 라이브 상태로 변경됐습니다.\n");
					videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
					log.append("\t\t\t Upcoming 테이블에서 삭제\n");

					upcoming.setActualStartTime(format.format(liveStreaming.getActualStartTime().getValue()));
					videoDao.createLive(upcoming);
					log.append("\t\t\t Live 테이블에 삽입\n");
				}
				else {
					if (format.format(liveStreaming.getScheduledStartTime().getValue()).compareTo(upcoming.getScheduledStartTime()) != 0) {
						log.append("\t\t" + upcoming.getMemberName() + "의 예약된 동영상의 예정 시작 시간이 변경되었습니다.\n");
						upcoming.setScheduledStartTime(format.format(liveStreaming.getScheduledStartTime().getValue()));
						videoDao.updateScheduledStartTime(upcoming);
						log.append("\t\t\t" + upcoming.getMemberName() + "Upcoming 테이블에서 예약 시작 시간 변경 완료\n");
					}
					else
						log.append("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 아직 예약 상태입니다.\n");
				}
					
			}
		} catch (IOException ioe) {
			log.append("API 요청 시 오류가 발생했습니다.\n");
		}
		
		return log.toString();
	}
}

