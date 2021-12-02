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
import com.hololive.livestream.DAO.VideoDAO;
import com.hololive.livestream.DTO.APIDTO;
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.Video;
import com.hololive.livestream.DTO.VideoDTO;

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
	private VideoDAO videoDao;

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		System.out.println("\n" + (format.format(Calendar.getInstance().getTime()) + " : checkChannel"));

		List<MemberDTO> memberList = videoDao.readAllMember();

		String channelURL = "https://holodex.net/api/v2/users/live?channels=" + memberList.get(0).getChannelId();
		for (int i = 1; i < memberList.size(); i++)
			channelURL += "," + memberList.get(i).getChannelId();

		format = new SimpleDateFormat("yy.MM.dd HH:mm");
		APIDTO apiKey = videoDao.readMinQuotasAPIKey();
		videoDao.increaseQuotas100(apiKey.getApiKey());
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(channelURL))
				.header("Content-Type", "application/json").header("X-APIKEY", apiKey.getApiKey())
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			List<Video> videos = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Video.class));
			videoDao.setAllRefreshedFalse();
			for (int j = 0; j < videos.size(); j++) {
				Video video = videos.get(j);
				if (video.getStatus().equals("upcoming")) {								// 예약 상태라면 Upcoming에 삽입
					Calendar now = Calendar.getInstance();
					now.add(Calendar.DATE, 2);
					String limit = format.format(now.getTime());
					if (video.getAvailable_at().compareTo(limit) <= 0) {
						VideoDTO reserved = new VideoDTO();
						reserved.setMemberName(videoDao.readMemberNameByChannelId(video.getChannel().getId()));
						reserved.setChannelId(video.getChannel().getId());
						reserved.setScheduledStartTime(video.getAvailable_at());
						reserved.setProfilePath(video.getChannel().getPhoto());
						reserved.setVideoId(video.getId());
						reserved.setThumbnailPath("https://i.ytimg.com/vi/" + video.getId() + "/mqdefault.jpg");
						System.out.println("\t" + video.getChannel().getId() + " : -> Upcoming");
						videoDao.createUpcoming(reserved);
					}
					else
						System.out.println("\t" + video.getChannel().getId() + " : X-> Upcoming \n\t\t 너무 뒤에 예약되어 있습니다.(" + video.getAvailable_at() + ", " + limit + ")");
				}
				else if (video.getStatus().equals("live")) {							// 라이브 상태라면 Live에 삽입
					VideoDTO live = videoDao.readUpcomingByVideoId(video.getId());
					if (live != null) {
						live.setActualStartTime(video.getAvailable_at());
					}
					else {
						live = new VideoDTO();
						live.setMemberName(videoDao.readMemberNameByChannelId(video.getChannel().getId()));
						live.setChannelId(video.getChannel().getId());
						live.setActualStartTime(video.getAvailable_at());
						live.setProfilePath(video.getChannel().getPhoto());
						live.setVideoId(video.getId());
						live.setThumbnailPath("https://i.ytimg.com/vi/" + video.getId() + "/mqdefault.jpg");
					}
					System.out.println("\t" + video.getChannel().getId() + " : Upcoming -> Live");
					videoDao.deleteUpcomingByVideoId(video.getId());
					videoDao.createLive(live);
				}
			}
			
			List<VideoDTO> completeLive = videoDao.readAllInLiveNotRefreshed();
			for (int j = 0; j < completeLive.size(); j++) {
				VideoDTO completed = completeLive.get(j);
				System.out.println("\t" + completed.getVideoId() + " : Live -> Completed");
				videoDao.deleteLiveByVideoId(completed.getVideoId());
				videoDao.createCompleted(completed);
			}
			
		} catch (Exception e) {
			System.out.println("\t\t" + e.getMessage() + "\n");
		}
	}
}
