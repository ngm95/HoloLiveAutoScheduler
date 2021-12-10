package com.hololive.livestream.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hololive.livestream.DAO.VideoDAO;
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.DTO.VideoVue;

@Service
public class VideoService {
	
	@Autowired
	private VideoDAO videoDao;
	
	public List<VideoVue> readAllInUpcoming() {
		return videoDao.readAllInUpcoming();
	}
	
	public List<VideoVue> readAllInUpcomingIn1Hour() {
		return videoDao.readAllInUpcomingIn1Hour();
	}
	
	public List<VideoVue> readAllInLive() {
		return videoDao.readAllInLive();
	}
	
	public List<VideoVue> readAllInCompletedIn3Days() {
		return videoDao.readAllInCompletedIn3Days();
	}
	
	public List<VideoVue> readAllInCompletedBetweenSomeday(String start, String end, int offset) {
		return videoDao.readAllInCompletedBetweenSomeday(start, end, offset);
	}
	
	public int readAllInCompletedSize(String start, String end) {
		return videoDao.readAllInCompletedSize(start, end);
	}
	
	
	public void createUpcoming(VideoDTO videoDto) {
		videoDao.createUpcoming(videoDto);
	}
	
	public void createLive(VideoDTO videoDto) {
		videoDao.createLive(videoDto);
	}
	
	public void createCompleted(VideoDTO videoDto) {
		videoDao.createCompleted(videoDto);
	}
	
	public List<MemberDTO> readAllMember() {
		return videoDao.readAllMember();
	}
	
	public VideoDTO readUpcomingByVideoId(String videoId) {
		return videoDao.readUpcomingByVideoId(videoId);
	}
	
	public String readMemberNameByChannelId(String channelId) {
		return videoDao.readMemberNameByChannelId(channelId);
	}
	
	public List<VideoDTO> readAllInUpcomingNotRefreshed() {
		return videoDao.readAllInUpcomingNotRefreshed();
	}
	
	public List<VideoDTO> readAllInLiveNotRefreshed() {
		return videoDao.readAllInLiveNotRefreshed();
	}
	
	public void deleteUpcomingByVideoId(String videoId) {
		videoDao.deleteUpcomingByVideoId(videoId);;
	}
	
	public void deleteLiveByVideoId(String videoId) {
		videoDao.deleteLiveByVideoId(videoId);
	}
	
	public void setUpcomingAllRefreshedFalse() {
		videoDao.setUpcomingAllRefreshedFalse();
	}
	
	public void setLiveAllRefreshedFalse() {
		videoDao.setLiveAllRefreshedFalse();
	}
	
	
	public VideoDTO readCompletedByVideoId(String videoId) {
		return videoDao.readCompletedByVideoId(videoId);
	}
}
