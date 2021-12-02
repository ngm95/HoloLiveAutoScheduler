package com.hololive.livestream.Quartz;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hololive.livestream.DAO.VideoDAO;
import com.hololive.livestream.DTO.APIDTO;
import com.hololive.livestream.DTO.Video;
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

	@Autowired
	private VideoDAO videoDao;

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		StringBuilder log = new StringBuilder();
		log.append(format.format(Calendar.getInstance().getTime()) + " : checkLive\n");

		// DB Live 테이블에 있는 데이터를 리스트로 가져옴
		List<VideoDTO> liveList = videoDao.readAllInLive();

		for (int i = 0; i < liveList.size(); i++) {
			VideoDTO live = liveList.get(i);
			String videoURL = "https://holodex.net/api/v2/videos?channel_id=" + live.getChannelId() + "&id="
					+ live.getVideoId();
			APIDTO apiKey = videoDao.readMinQuotasAPIKey();
			videoDao.increaseQuotas1(apiKey.getApiKey());

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(videoURL))
					.header("Content-Type", "application/json").header("X-APIKEY", apiKey.getApiKey())
					.method("GET", HttpRequest.BodyPublishers.noBody()).build();
			HttpResponse<String> response;
			try {
				response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				Video video = objectMapper.readValue(response.body().substring(1, response.body().length()-1), Video.class);
				if (!video.getStatus().equals("live")) { 	// 라이브 상태가 해제되면 live->completed로 옮긴다.
					VideoDTO completed = new VideoDTO();
					completed.setMemberName(videoDao.readMemberNameByChannelId(video.getChannel().getId()));
					completed.setChannelId(video.getChannel().getId());
					completed.setScheduledStartTime(video.getStart_scheduled());
					completed.setActualStartTime(video.getStart_actual());
					completed.setActualEndTime(video.getEnd_actual());
					completed.setProfilePath(video.getChannel().getPhoto());
					completed.setVideoId(video.getId());
					completed.setThumbnailPath("https://i.ytimg.com/vi/" + video.getId() + "/mqdefault_live.jpg");
					log.append("\t" + video.getChannel().getId() + " 종료 : Completed 테이블로 넣습니다.\n");
					
					videoDao.deleteLiveByVideoId(video.getId());
					videoDao.createCompleted(completed);
				}
			} catch (IOException | InterruptedException e) {
				log.append("\t\t" + e.getMessage() + "\n");
			}
		}

		System.out.println(log);
	}

}
