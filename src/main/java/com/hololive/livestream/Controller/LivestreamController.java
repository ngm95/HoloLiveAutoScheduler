package com.hololive.livestream.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.Service.VideoService;

@Controller
@RequestMapping("/livestream")
public class LivestreamController {
	
	@Autowired
	VideoService videoServ;
	
	@RequestMapping("/schedule")
	public String schedule(Model model) {
		List<MemberDTO> notReservedMembers = videoServ.readNotInUpcomingOrLive();
		List<VideoDTO> upcomingList = videoServ.readAllInUpcoming();
		List<VideoDTO> liveList = videoServ.readAllInLive();
		List<VideoDTO> completedList = videoServ.readAllInCompletedIn2Days();
		
		List<VideoDTO> videos = new ArrayList<>();
		
		for (VideoDTO completed : completedList)
			videos.add(completed);
		for (VideoDTO live : liveList)
			videos.add(live);
		for (VideoDTO upcoming : upcomingList)
			videos.add(upcoming);
		
		model.addAttribute("upcomingList", upcomingList);
		model.addAttribute("liveList", liveList);
		model.addAttribute("completedList", completedList);
		
		model.addAttribute("notReservedMembers", notReservedMembers);
		model.addAttribute("videos", videos);
		
		return "/schedule";
	}
}
