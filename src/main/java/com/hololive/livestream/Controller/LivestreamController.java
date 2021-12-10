package com.hololive.livestream.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hololive.livestream.DTO.VideoVue;
import com.hololive.livestream.Service.VideoService;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping("/livestream")
public class LivestreamController {
	
	@Autowired
	VideoService videoServ;
	
	/**
	 * 현재 방송 중인 영상과 1시간 내에 시작할 영상을 리스트에 저장해서
	 * Gson 객체를 사용해 JSON형식의 문자열로 변환해 반환한다.
	 * 
	 * 반환된 JSON 문자열을 받은 View에서는 이것을 이용해 프로필 이미지를 출력하고
	 * 멀티뷰 페이지에서 클릭하면 영상을 바로 볼 수 있도록 구성한다.
	 * 
	 * @return 해당하는 객체 리스트의 JSON형식 문자열
	 */
	@GetMapping("/videoInfo")
	@ResponseBody
	public String videoInfo() {
		List<VideoVue> liveList = videoServ.readAllInLive();
		List<VideoVue> upcomingList = videoServ.readAllInUpcomingIn1Hour();
		
		List<VideoVue> videos = new ArrayList<>();
		for (VideoVue live : liveList)
			videos.add(live);
		for (VideoVue upcoming : upcomingList)
			videos.add(upcoming);
		
		return new Gson().toJson(videos);
	}
	
	@GetMapping("/getLiveList")
	@ResponseBody
	public String getLiveList() {
		return new Gson().toJson(videoServ.readAllInLive());
	}
	
	@GetMapping("/getUpcomingList")
	@ResponseBody
	public String getUpcomingList() {
		return new Gson().toJson(videoServ.readAllInUpcoming());
	}
	
	@GetMapping("/getCompletedListIn3Day")
	@ResponseBody
	public String getCompletedListIn3Day() {
		return new Gson().toJson(videoServ.readAllInCompletedIn3Days());
	}
	
	@GetMapping("/getCompletedListSize/{start}/{end}")
	@ResponseBody
	public String getCompletedListBetweenSomeday(@PathVariable("start") String start, @PathVariable("end") String end) {
		start = start.concat(" 00:00:00");
		end = end.concat(" 23:59:59");
		return Integer.toString(videoServ.readAllInCompletedSize(start, end));
	}
	
	@GetMapping("/getCompletedListBetweenSomeday/{start}/{end}/{offset}")
	@ResponseBody
	public String getCompletedListBetweenSomeday(@PathVariable("start") String start, @PathVariable("end") String end, @PathVariable("offset") int offset) {
		start = start.concat(" 00:00:00");
		end = end.concat(" 23:59:59");
		return new Gson().toJson(videoServ.readAllInCompletedBetweenSomeday(start, end, offset));
	}
}
