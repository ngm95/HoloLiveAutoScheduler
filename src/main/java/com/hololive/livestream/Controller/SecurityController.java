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
			videoServ.checkUpcoming();
			videoServ.checkLive();
			doItBefore = true;
		}
		
		return "redirect:/";
	}
}
