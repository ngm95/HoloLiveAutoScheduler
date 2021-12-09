package com.hololive.livestream.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoAPI;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.Service.VideoService;

@Controller
public class MainPageController {

	@RequestMapping("/")
	public String mainPage() {
		return "/index";
	}
	
	@RequestMapping("/collect")
	public String collect() {
		checkChannel();
		return "/collect";
	}

	@Autowired
	private VideoService videoServ;

	ObjectMapper objectMapper = new ObjectMapper();
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	@Async("executor")
	public void checkChannel() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<MemberDTO> members = videoServ.readAllMember();

		for (MemberDTO member : members) {
			System.out.println(member.getMemberName());
			int offset = 0;
			try {
				while (true) {
					long startTime = Calendar.getInstance().getTimeInMillis();
					System.out.print("\toffset : " + offset + " => ");
					
					String videoURL = "https://holodex.net/api/v2/videos?channel_id=" + member.getChannelId() + "&status=past&type=stream&include=live_info&order=asc&limit=50&offset=" + offset;
					
					HttpRequest request = HttpRequest.newBuilder().uri(URI.create(videoURL))
							.header("Content-Type", "application/json").header("X-APIKEY", "27e71dd9-f108-4896-875e-5a91207a63fe")
							.method("GET", HttpRequest.BodyPublishers.noBody()).build();
					HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
					List<VideoAPI> videosFromAPI = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, VideoAPI.class));
					if (videosFromAPI.size() == 0) {
						System.out.println("더 이상 영상이 없습니다.");
						break;
					}
					else {
						int recorded = 0;
						for (int i = 0; i < videosFromAPI.size(); i++) {
							VideoAPI videoAPI = videosFromAPI.get(i);
							
							if (videoServ.readCompletedByVideoId(videoAPI.getId()) != null)
								continue;
							
							VideoDTO completed = new VideoDTO();
							completed.setMemberName(member.getMemberName());
							completed.setChannelId(member.getChannelId());
							completed.setVideoId(videoAPI.getId());
							completed.setScheduledStartTime(videoAPI.getStart_scheduled());
							completed.setActualStartTime(videoAPI.getStart_actual());
							completed.setActualEndTime(videoAPI.getEnd_actual());
							
							if (completed.getActualStartTime() == null)
								continue;
							
							videoServ.createCompleted(completed);
							recorded++;
						}
						long endTime = Calendar.getInstance().getTimeInMillis();
						System.out.println("총 " + recorded + "개가 입력되었습니다. (동작 시간 : " + (endTime-startTime) + ")");
						offset += 50;
					}
					
					Thread.sleep(1000);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
