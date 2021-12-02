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
 *         예약된 영상들을 체크해서 상태가 변경되었는지 검사하는 QuartzJobBean 매시 0분 0초부터 10분 간격으로 실행하므로
 *         하루에 총 480번 실행됨
 * 
 *         실행 간격이 짧기 때문에 불필요한 API 요청을 줄이기 위해 현재 시간이 예약된 시간을 넘은 영상들에 대해서만 검사를 실행함
 */
@Component
public class CheckUpcoming extends QuartzJobBean {

	@Autowired
	private VideoDAO videoDao;

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
			else {
				log.append(checkAlive(upcoming, format));
			}
		}

		System.out.println(log);
	}

	public String checkStart(VideoDTO upcoming, DateFormat format) {
		StringBuilder log = new StringBuilder();

		String videoURL = "https://holodex.net/api/v2/videos?channel_id=" + upcoming.getChannelId() + "&id="
				+ upcoming.getVideoId();
		APIDTO apiKey = videoDao.readMinQuotasAPIKey();
		videoDao.increaseQuotas1(apiKey.getApiKey());

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(videoURL))
				.header("Content-Type", "application/json").header("X-APIKEY", apiKey.getApiKey())
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			Video video = objectMapper.readValue(response.body().substring(response.body().length()), Video.class);
			if (video == null) {
				videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
			}
			else if (video.getStatus().equals("live")) { 				// 라이브 상태면 Upcoming -> Live로 옮긴다.
				upcoming.setActualStartTime(video.getAvailable_at());
				log.append("\t" + video.getChannel().getId() + " 시작 : Live 테이블로 넣습니다.\n");

				videoDao.deleteUpcomingByVideoId(video.getId());
				videoDao.createLive(upcoming);
			}
			else if (video.getStatus().equals("past")) {				// 종료되었다면 Upcoming -> Completed
				upcoming.setActualStartTime(video.getAvailable_at());
			}
		} catch (IOException | InterruptedException e) {
			log.append("\t\t" + e.getMessage() + "\n");
		}

		return log.toString();
	}
	
	public String checkAlive(VideoDTO upcoming, DateFormat format) {
		StringBuilder log = new StringBuilder();

		String videoURL = "https://holodex.net/api/v2/videos?channel_id=" + upcoming.getChannelId() + "&id="
				+ upcoming.getVideoId();
		APIDTO apiKey = videoDao.readMinQuotasAPIKey();
		videoDao.increaseQuotas1(apiKey.getApiKey());

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(videoURL))
				.header("Content-Type", "application/json").header("X-APIKEY", apiKey.getApiKey())
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			Video video = objectMapper.readValue(response.body().substring(response.body().length()), Video.class);
			if (video == null) {
				videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
			} 
			else if (video.getStatus().equals("upcoming") && !video.getAvailable_at().equals(upcoming.getScheduledStartTime())) {
				upcoming.setScheduledStartTime(video.getAvailable_at());
				videoDao.updateScheduledStartTime(upcoming);
			}
			else if (!video.getStatus().equals("upcoming")) {
				videoDao.deleteUpcomingByVideoId(video.getId());
			}
		} catch (IOException | InterruptedException e) {
			log.append("\t\t" + e.getMessage() + "\n");
		}

		return log.toString();		
	}
}
