package com.hololive.livestream.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hololive.livestream.Service.VideoService;

@Controller
@RequestMapping("/security")
public class SecurityController {
	@Autowired
	private VideoService videoServ;
	
	private boolean doItBefore = false;
	
	@RequestMapping("/threads")
	public String addThreads() {
		if (!doItBefore) {
			videoServ.checkChannel();
			videoServ.checkUpcoming();
			videoServ.checkLive();
			doItBefore = true;
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/channel")
	public String checkChannel() {
		videoServ.checkChannel();
		
		return "redirect:/";
	}
	
	@RequestMapping("/live")
	public String checkLive() {
		videoServ.checkLive();
		
		return "redirect:/";
	}
	
	@RequestMapping("/upcoming")
	public String checkUpcoming() {
		videoServ.checkUpcoming();
		
		return "redirect:/";
	}
}
