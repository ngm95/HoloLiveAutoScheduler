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

@Component
public class CheckUpcomingAlive extends QuartzJobBean {
	
	@Autowired
	private VideoDAO videoDao;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
			Video video = objectMapper.readValue(response.body().substring(1, response.body().length()-1), Video.class);
			if (video == null) {
				videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
			} 
			else if (!video.getStart_scheduled().equals(upcoming.getScheduledStartTime())) {
				upcoming.setScheduledStartTime(video.getStart_scheduled());
				videoDao.updateScheduledStartTime(upcoming);
			}
		} catch (IOException | InterruptedException e) {
			log.append("\t\t" + e.getMessage() + "\n");
		}

		return log.toString();		
	}
}
