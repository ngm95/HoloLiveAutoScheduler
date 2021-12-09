package com.hololive.livestream.Quartz;

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
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoAPI;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.Service.VideoService;

/**
 * @author ngm95
 *
 *         정해진 유튜브 채널을 체크해서 새로 예약된 영상이 있는지 검사하는 QuartzJobBean 매시 0분 0초에 실행하므로
 *         하루에 총 24번 실행됨
 * 
 *         1시간 이전부터 현재 시간까지 예약된 동영상이 있으면 영상들을 모두 Upcoming 테이블에 저장한다.
 */
@Component
public class CheckChannel extends QuartzJobBean {

	@Autowired
	private VideoService videoServ;

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		System.out.println("\n" + (format.format(Calendar.getInstance().getTime()) + " : checkChannel"));

		List<MemberDTO> memberList = videoServ.readAllMember();

		String channelURL = "https://holodex.net/api/v2/users/live?channels=" + memberList.get(0).getChannelId();
		for (int i = 1; i < memberList.size(); i++)
			channelURL += "," + memberList.get(i).getChannelId();

		String apiKey = "da6221ef-1dd7-453d-8507-8bf11ae45146";
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(channelURL))
				.header("Content-Type", "application/json").header("X-APIKEY", apiKey)
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			List<VideoAPI> videos = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, VideoAPI.class));
			videoServ.setUpcomingAllRefreshedFalse();
			videoServ.setLiveAllRefreshedFalse();
			for (int j = 0; j < videos.size(); j++) {
				VideoAPI video = videos.get(j);
				if (video.getStatus().equals("upcoming")) {								// 예약 상태라면 Upcoming에 삽입
					Calendar limit = Calendar.getInstance();
					limit.add(Calendar.DATE, 2);
					if (video.getAvailable_at().before(limit.getTime())) {
						VideoDTO reserved = new VideoDTO();
						reserved.setMemberName(videoServ.readMemberNameByChannelId(video.getChannel().getId()));
						reserved.setChannelId(video.getChannel().getId());
						reserved.setScheduledStartTime(video.getAvailable_at());
						reserved.setProfilePath(video.getChannel().getPhoto());
						reserved.setVideoId(video.getId());
						System.out.println("\t" + video.getChannel().getId() + " : -> Upcoming");
						videoServ.createUpcoming(reserved);
					}
					else
						System.out.println("\t" + video.getChannel().getId() + " : X-> Upcoming \n\t\t 너무 뒤에 예약되어 있습니다.(" + video.getAvailable_at() + ", " + limit + ")");
				}
				else if (video.getStatus().equals("live")) {							// 라이브 상태라면 Live에 삽입
					VideoDTO live = videoServ.readUpcomingByVideoId(video.getId());
					if (live != null) {
						live.setActualStartTime(video.getAvailable_at());
					}
					else {
						live = new VideoDTO();
						live.setMemberName(videoServ.readMemberNameByChannelId(video.getChannel().getId()));
						live.setChannelId(video.getChannel().getId());
						live.setActualStartTime(video.getAvailable_at());
						live.setProfilePath(video.getChannel().getPhoto());
						live.setVideoId(video.getId());
					}
					System.out.println("\t" + video.getChannel().getId() + " : Upcoming -> Live");
					videoServ.deleteUpcomingByVideoId(video.getId());
					videoServ.createLive(live);
				}
			}
			
			List<VideoDTO> completeUpcoming = videoServ.readAllInUpcomingNotRefreshed();
			for (int j = 0; j < completeUpcoming.size(); j++) {
				VideoDTO completed = completeUpcoming.get(j);
				System.out.println("\t" + completed.getVideoId() + " : Upcoming -> Completed");
				completed.setActualStartTime(completed.getScheduledStartTime());
				videoServ.deleteUpcomingByVideoId(completed.getVideoId());
				videoServ.createCompleted(completed);
			}
			
			List<VideoDTO> completeLive = videoServ.readAllInLiveNotRefreshed();
			for (int j = 0; j < completeLive.size(); j++) {
				VideoDTO completed = completeLive.get(j);
				System.out.println("\t" + completed.getVideoId() + " : Live -> Completed");
				videoServ.deleteLiveByVideoId(completed.getVideoId());
				videoServ.createCompleted(completed);
			}
			
		} catch (Exception e) {
			System.out.println("\t\t" + e.getMessage() + "\n");
		}
	}
}
