package com.hololive.livestream.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.Service.VideoService;

@Controller
@RequestMapping("/livestream")
public class LivestreamController {
	
	@ModelAttribute("upcomingList")
	public List<VideoDTO> upcomingList() {
		return videoServ.readAllInUpcoming();
	}
	
	@ModelAttribute("liveList")
	public List<VideoDTO> liveList() {
		return videoServ.readAllInLive();
	}
	
	@ModelAttribute("completedList")
	public List<VideoDTO> completedList(HttpServletRequest request) {
		if (request.getRequestURI().equals("/livestream/schedule"))
			return videoServ.readAllInCompletedIn1Days();
		else
			return videoServ.readAllInCompleted();
	}
	
	@Autowired
	VideoService videoServ;
	
	/**
	 * 방송 중인 영상, 예약된 영상, 하루 내에 종료한 영상 정보를 각각 모델에 담고
	 * 스케줄표를 볼 수 있는 페이지로 넘어간다.
	 */
	@RequestMapping("/schedule")
	public String schedule(Model model) {
		return "/schedule";
	}
	
	@RequestMapping("/multiview")
	public String multiview(Model model) {
		return "/multiview";
	}
	
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
		List<VideoDTO> liveList = videoServ.readAllInLive();
		List<VideoDTO> upcomingList = videoServ.readAllInUpcomingIn1Hour();
		
		List<VideoDTO> videos = new ArrayList<>();
		for (VideoDTO live : liveList)
			videos.add(live);
		for (VideoDTO upcoming : upcomingList)
			videos.add(upcoming);
		
		Gson gson = new Gson();
		return gson.toJson(videos);
	}
}
