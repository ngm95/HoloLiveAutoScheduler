package com.hololive.livestream.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;

@Service
public class VideoService {
	
	@Autowired
	private VideoDAO videoDao;
	
	public List<MemberDTO> readNotInUpcomingOrLive() {
		return videoDao.readNotInUpcomingOrLive();
	}
	
	public List<VideoDTO> readAllInUpcoming() {
		return videoDao.readAllInUpcoming();
	}
	
	public List<VideoDTO> readAllInLive() {
		return videoDao.readAllInLive();
	}
	
	public List<VideoDTO> readAllInUpcomingIn1Day() {
		return videoDao.readAllInUpcomingIn1Day();
	}
	
	public List<VideoDTO> readAllInCompletedIn2Days() {
		return videoDao.readAllInCompletedIn2Days();
	}
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
		public void initialize(HttpRequest request) throws IOException {}
	}).setApplicationName("HoloLiveAutoScheduler").build();
	
	@Async("executor")
	public void checkChannel() {
		while (true) {
			
			System.out.println(Calendar.getInstance().getTime() + " - checkChannel : " + Thread.currentThread().getName() + ", " + Thread.currentThread().getId());
			
			// DB에서 멤버 리스트를 가져옴
			List<MemberDTO> memberList = videoDao.readAllMember();
			System.out.println("\t member : " + memberList);
			
			// 각 리스트마다 Search:list로 예약 동영상이 있는지 확인
			for (MemberDTO member : memberList) {
				System.out.println("\t member : " + member.getMemberName());
				try {
					YouTube.Search.List search = youtube.search().list("id,snippet");
					search.setFields("items(id/videoId, snippet/title, snippet/thumbnails/medium/url)");

					search.setMaxResults(1L);
					search.setChannelId(member.getChannelId());
					search.setEventType("upcoming");
					search.setType("video");
					search.setOrder("date");
					search.setKey(member.getApiKey());
					
					Calendar findLimit = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
					
					findLimit.add(Calendar.HOUR_OF_DAY, -1);		// 1시간 전~지금까지 예약된 동영상 하나를 받아옴
					findLimit.add(Calendar.MINUTE, -1);				// 스레드가 작동되는 시간을 고려해 1분의 여유를 둠
					search.setPublishedAfter(new DateTime(format.format(findLimit.getTime()).toString()));
					
					SearchListResponse searchResponse = search.execute();
					List<SearchResult> searchResultList = searchResponse.getItems();
					
					// 예약된 동영상이 있으면 DB의 Upcoming 테이블에 저장
					if (!searchResultList.isEmpty()) {
						SearchResult searchResult = searchResultList.get(0);
						ResourceId rId = searchResultList.get(0).getId();
						
						YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
						videos.setFields("items(id, liveStreamingDetails/scheduledStartTime)");
						videos.setKey(member.getApiKey());     
						videos.setId(rId.getVideoId());
						videos.setMaxResults(1L); 
						
						List<Video> videoList = videos.execute().getItems();
						VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
						
						format = new SimpleDateFormat("yy.MM.dd HH:mm");		
						
						String scheduledStartTime = format.format(liveStreaming.getScheduledStartTime().getValue());
						
						System.out.println("\t\t" + member.getMemberName() + "의 예약된 동영상이 있습니다.");
						videoDao.createUpcoming(new VideoDTO(member.getMemberName(), member.getChannelId(), searchResult.getId().getVideoId(), scheduledStartTime, searchResult.getSnippet().getThumbnails().getMedium().getUrl(), member.getApiKey()));
						System.out.println("\t\t" + "Upcoming 테이블에 저장 완료");
					
					}
					else {
						System.out.println("\t\t" + member.getMemberName() + "의 예약된 동영상이 없습니다.");
					}
				} catch (IOException ioe) {
					System.out.println("API 요청 시 오류가 발생했습니다.");
				}
			}
			try {
				// 1시간동안 휴식
				Thread.sleep(1000*60*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Async("executor")
	public void checkUpcoming() {
		while (true) {
			try {
				// 2.5분동안 휴식
				Thread.sleep(1000*30*5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(Calendar.getInstance().getTime() + " - checkUpcoming : " + Thread.currentThread().getName() + ", " + Thread.currentThread().getId());
			
			// DB Upcoming 테이블에 있는 데이터를 리스트로 가져옴
			List<VideoDTO> upcomingList = videoDao.readAllInUpcoming();

			DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
			
			// Upcoming 동영상의 예정 시간이 지났다면 Videos:list로 확인
			for (VideoDTO upcoming : upcomingList) {	
				if (upcoming.getScheduledStartTime().compareTo(format.format(Calendar.getInstance().getTime())) <= 0) {
					checkStart(upcoming, format);
				}
			}
			
			try {
				// 2.5분동안 휴식
				Thread.sleep(1000*30*5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void checkStart(VideoDTO upcoming, DateFormat format) {
		System.out.println("\t member : " + upcoming.getMemberName());
		try {
			YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
			videos.setFields("items(id, liveStreamingDetails/scheduledStartTime, liveStreamingDetails/actualStartTime)");
			videos.setKey(upcoming.getApiKey());     
			videos.setId(upcoming.getVideoId());
			videos.setMaxResults(1L); 

			List<Video> videoList = videos.execute().getItems();

			if (videoList.isEmpty()) {						// 반환 결과가 통째로 null이면 예약이 취소된 경우이므로 Upcoming테이블에서 삭제
				System.out.println("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 취소됐습니다.");
				videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
				System.out.println("\t\t Upcoming 테이블에서 삭제");
			} else {										// actualStartTime!=null이면 Upcoming 테이블에서 삭제하고 Live 테이블에 저장
				VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
				if (liveStreaming.getActualStartTime() != null) {
					System.out.println("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 라이브 상태로 변경됐습니다.");
					videoDao.deleteUpcomingByVideoId(upcoming.getVideoId());
					System.out.println("\t\t Upcoming 테이블에서 삭제");

					upcoming.setActualStartTime(format.format(liveStreaming.getActualStartTime().getValue()));
					videoDao.createLive(upcoming);
					System.out.println("\t\t Live 테이블에 삽입");
				}
				else {
					if (format.format(liveStreaming.getScheduledStartTime()).compareTo(upcoming.getScheduledStartTime()) != 0) {
						System.out.println("\t\t" + upcoming.getMemberName() + "의 예약된 동영상의 예정 시작 시간이 변경되었습니다.");
						upcoming.setScheduledStartTime(format.format(liveStreaming.getScheduledStartTime()));
						videoDao.updateScheduledStartTime(upcoming);
						System.out.println("\t\t" + upcoming.getMemberName() + "Upcoming 테이블에서 예약 시작 시간 변경 완료");
					}
					else
						System.out.println("\t\t" + upcoming.getMemberName() + "의 예약된 동영상이 아직 예약 상태입니다.");
				}
					
			}
		} catch (IOException ioe) {
			System.out.println("API 요청 시 오류가 발생했습니다.");
		}
	}
	
	@Async("executor")
	public void checkLive() {
		while (true) {
			try {
				// 4분동안 휴식
				Thread.sleep(1000*60*4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(Calendar.getInstance().getTime() + " - checkLive : " + Thread.currentThread().getName() + ", " + Thread.currentThread().getId());
			
			// DB Live 테이블에 있는 데이터를 리스트로 가져옴
			List<VideoDTO> liveList = videoDao.readAllInLive();
			System.out.println("\t live : " + liveList);
			
			// Live 동영상에 대해 Videos:list로 확인
			for (VideoDTO live : liveList) {
				System.out.println("\t member : " + live.getMemberName());
				try {
					YouTube.Videos.List videos = youtube.videos().list("liveStreamingDetails");
					videos.setFields("items(id, liveStreamingDetails/scheduledStartTime, liveStreamingDetails/actualStartTime, liveStreamingDetails/actualEndTime)");
					videos.setKey(live.getApiKey());     
					videos.setId(live.getVideoId());
					videos.setMaxResults(1L); 

					List<Video> videoList = videos.execute().getItems();

					if (videoList.isEmpty()) {						// 반환 결과가 통째로 null이면 동영상이 삭제되었거나 아카이브가 올라가기 이전이므로 Live 테이블에서 삭제하고 Completed 테이블에 저장
						System.out.println("\t\t" + live.getMemberName() + "의 라이브 동영상이 종료되었습니다.");
						videoDao.deleteLiveByVideoId(live.getVideoId());
						System.out.println("\t\t Live 테이블에서 삭제");
						videoDao.createCompleted(live);
						System.out.println("\t\t Completed 테이블에 삽입");
					} else {										// actualEndTime!=null이면 Live 테이블에서 삭제하고 Completed 테이블에 저장
						VideoLiveStreamingDetails liveStreaming = videoList.get(0).getLiveStreamingDetails();
						if (liveStreaming.getActualEndTime() != null) {
							System.out.println("\t\t" + live.getMemberName() + "의 라이브 동영상이 종료되었습니다.");
							videoDao.deleteLiveByVideoId(live.getVideoId());
							System.out.println("\t\t Live 테이블에서 삭제");

							DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
							live.setActualEndTime(format.format(liveStreaming.getActualEndTime().getValue()));
							videoDao.createCompleted(live);
							System.out.println("\t\t Completed 테이블에 삽입");
						}
						else
							System.out.println("\t\t" + live.getMemberName() + "의 라이브 동영상이 아직 라이브 상태입니다.");
					}
				} catch (IOException ioe) {
					System.out.println("API 요청 시 오류가 발생했습니다.");
				}
			}
			try {
				// 6분동안 휴식
				Thread.sleep(1000*60*6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
