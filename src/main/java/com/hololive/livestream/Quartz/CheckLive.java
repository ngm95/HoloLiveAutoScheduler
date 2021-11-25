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
 *         방중 중인 영상들을 체크해서 상태가 변경되었는지 검사하는 QuartzJobBean 매시 0분 0초부터 20분 간격으로
 *         실행하므로 하루에 총 72번 실행됨
 * 
 * 
 */
@Component
public class CheckLive extends QuartzJobBean {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
		public void initialize(HttpRequest request) throws IOException {
		}
	}).setApplicationName("HoloLiveAutoScheduler").build();

	@Autowired
	private VideoDAO videoDao;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		StringBuilder log = new StringBuilder();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		log.append(format.format(Calendar.getInstance().getTime()) + " : checkLive\n");

		// DB Live 테이블에 있는 데이터를 리스트로 가져옴
		List<VideoDTO> liveList = videoDao.readAllInLive();

		// Live 동영상에 대해 Videos:list로 확인
		for (VideoDTO live : liveList) {
			log.append("\t member : " + live.getMemberName() + "\n");
			try {
				YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
				videos.setFields(
						"items(id, liveStreamingDetails/scheduledStartTime, liveStreamingDetails/actualStartTime, liveStreamingDetails/actualEndTime)");
				videos.setKey(live.getApiKey());
				videos.setId(live.getVideoId());
				videos.setMaxResults(1L);

				videoDao.increaseQuotas1(live.getApiKey());
				List<Video> videoList = videos.execute().getItems();

				if (videoList.isEmpty()) { // 반환 결과가 통째로 null이면 동영상이 삭제되었거나 아카이브가 올라가기 이전이므로 Live 테이블에서 삭제하고 Completed
											// 테이블에 저장
					log.append("\t\t" + live.getMemberName() + "의 라이브 동영상이 종료되었습니다.\n");
					videoDao.deleteLiveByVideoId(live.getVideoId());
					log.append("\t\t\t Live 테이블에서 삭제\n");
					videoDao.createCompleted(live);
					log.append("\t\t\t Completed 테이블에 삽입\n");
				} else { // actualEndTime!=null이면 Live 테이블에서 삭제하고 Completed 테이블에 저장
					VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
					if (liveStreaming.getActualEndTime() != null) {
						log.append("\t\t" + live.getMemberName() + "의 라이브 동영상이 종료되었습니다.\n");
						videoDao.deleteLiveByVideoId(live.getVideoId());
						log.append("\t\t\t Live 테이블에서 삭제\n");

						format = new SimpleDateFormat("yy.MM.dd HH:mm");
						live.setActualEndTime(format.format(liveStreaming.getActualEndTime().getValue()));
						videoDao.createCompleted(live);
						log.append("\t\t\t Completed 테이블에 삽입\n");
					} else
						log.append("\t\t" + live.getMemberName() + "의 라이브 동영상이 아직 라이브 상태입니다.\n");
				}
			} catch (IOException ioe) {
				log.append("API 요청 시 오류가 발생했습니다.\n");
			}
		}

		System.out.println(log);
	}

}
