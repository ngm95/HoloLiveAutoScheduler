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
import com.hololive.livestream.DTO.APIDTO;
import com.hololive.livestream.DTO.VideoDTO;

@Component
public class CheckUpcomingAlive extends QuartzJobBean {
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
		log.append(format.format(Calendar.getInstance().getTime()) + " : checkUpcomingAlive\n");
		
		// DB Upcoming 테이블에 있는 데이터를 리스트로 가져옴
		List<VideoDTO> upcomingList = videoDao.readAllInUpcoming();

		format = new SimpleDateFormat("yy.MM.dd HH:mm");
		
		// Upcoming 동영상을 모두 검사
		for (VideoDTO upcoming : upcomingList) {	
			log.append(checkAlive(upcoming, format));
		}
		
		System.out.println(log);
	}

	public String checkAlive(VideoDTO upcoming, DateFormat format) {
		StringBuilder log = new StringBuilder();
		
		log.append("\t member : " + upcoming.getMemberName() + "\n");
		try {
			YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
			videos.setFields("items(id, liveStreamingDetails/scheduledStartTime, liveStreamingDetails/actualStartTime)");
			    
			videos.setId(upcoming.getVideoId());
			videos.setMaxResults(1L); 

			APIDTO api = videoDao.readMinQuotasAPIKey();
			if (api.getQuota() > 9999)
				throw new RuntimeException("\t\t하루 할당량이 초과되었습니다.");
			videos.setKey(api.getApiKey()); 
			videoDao.increaseQuotas1(api.getApiKey());
			
			List<Video> videoList = videos.execute().getItems();

			// 반환 결과가 통째로 null이면 예약이 취소된 경우이므로 Upcoming테이블에서 삭제
			if (videoList.isEmpty()) {								
				log.append("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 취소됐습니다.\n");
				videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
				log.append("\t\t\t Upcoming 테이블에서 삭제\n");
			} else {												
				VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
				// actualStartTime==null이면 아직 예약되어 있는 상태
				if (liveStreaming.getActualStartTime() == null) {	
					// scheduledStartTime이 다르면 예정이 변경됨
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
