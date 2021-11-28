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
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;
import com.hololive.livestream.DAO.VideoDAO;
import com.hololive.livestream.DTO.APIDTO;
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;

/**
 * @author ngm95
 *
 * 정해진 유튜브 채널을 체크해서 새로 예약된 영상이 있는지 검사하는 QuartzJobBean
 * 매시 0분 0초에 실행하므로 하루에 총 24번 실행됨
 * 
 * 1시간 이전부터 현재 시간까지 예약된 동영상이 있으면 영상들을 모두 Upcoming 테이블에 저장한다.
 */
@Component
public class CheckChannel extends QuartzJobBean {
	
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
		log.append(format.format(Calendar.getInstance().getTime()) + " : checkChannel\n");
		
		Calendar findLimit = Calendar.getInstance();
		findLimit.add(Calendar.HOUR_OF_DAY, -1);		
		String oneHourBefore = format.format(findLimit.getTime()).toString();
		
		// DB에서 멤버 리스트를 가져옴
		List<MemberDTO> memberList = videoDao.readAllMember();
		
		format = new SimpleDateFormat("yy.MM.dd HH:mm");
		// 각 리스트마다 Search:list로 예약 동영상이 있는지 확인
		for (MemberDTO member : memberList) {
			log.append("\t member : " + member.getMemberName() + "\n");
			try {
				YouTube.Search.List search = youtube.search().list("id,snippet");
				search.setFields("items(id/videoId, snippet/title, snippet/thumbnails/medium/url)");

				search.setMaxResults(5L);
				search.setChannelId(member.getChannelId());
				search.setEventType("upcoming");
				search.setType("video");
				search.setOrder("date");
				
				search.setPublishedAfter(new DateTime(oneHourBefore));	// 1시간 전~지금까지 예약된 동영상 하나를 받아옴
				
				APIDTO api = videoDao.readMinQuotasAPIKey();
				if (api.getQuota() > 9900)
					throw new RuntimeException("\t\t하루 할당량이 초과되었습니다.");
				search.setKey(api.getApiKey());
				videoDao.increaseQuotas100(api.getApiKey());
				
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();
				
				// 예약된 동영상이 있으면 DB의 Upcoming 테이블에 저장
				if (!searchResultList.isEmpty()) {
					for (int i = 0; i < searchResultList.size(); i++) {
						SearchResult searchResult = searchResultList.get(i);
						ResourceId rId = searchResultList.get(i).getId();
						
						YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
						videos.setFields("items(id, liveStreamingDetails/scheduledStartTime)");
						videos.setId(rId.getVideoId());
						videos.setMaxResults(1L); 
						
						api = videoDao.readMinQuotasAPIKey();
						if (api.getQuota() > 9999)
							throw new RuntimeException("\t\t하루 할당량이 초과되었습니다.");
						videos.setKey(api.getApiKey());  
						videoDao.increaseQuotas1(api.getApiKey());
						
						List<Video> videoList = videos.execute().getItems();
						VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
						
						String scheduledStartTime = format.format(liveStreaming.getScheduledStartTime().getValue());
						
						log.append("\t\t" + member.getMemberName() + "의 예약된 동영상이 있습니다.\n");
						videoDao.createUpcoming(new VideoDTO(member.getMemberName(), member.getChannelId(), searchResult.getId().getVideoId(), scheduledStartTime, searchResult.getSnippet().getThumbnails().getMedium().getUrl()));
						log.append("\t\t\t" + "Upcoming 테이블에 저장 완료\n");
					}
				}
				else {
					log.append("\t\t" + member.getMemberName() + "의 예약된 동영상이 없습니다.\n");
				}
			} catch (IOException ioe) {
				log.append("API 요청 시 오류가 발생했습니다.\n");
			} catch (RuntimeException re) {
				log.append(re.getMessage());
			}
		}
		
		System.out.println(log);
	}
}
