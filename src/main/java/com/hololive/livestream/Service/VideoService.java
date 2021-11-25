package com.hololive.livestream.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<VideoDTO> readAllInUpcomingIn1Hour() {
		return videoDao.readAllInUpcomingIn1Hour();
	}
	
	public List<VideoDTO> readAllInCompletedIn1Days() {
		return videoDao.readAllInCompletedIn1Days();
	}
	
	public void increaseQuotas100(String apiKey) {
		videoDao.increaseQuotas100(apiKey);
	}
	
	public void increaseQuotas1(String apiKey) {
		videoDao.increaseQuotas1(apiKey);
	}
	
	public void resetQuota() {
		videoDao.resetQuota();
	}
	
	
}
