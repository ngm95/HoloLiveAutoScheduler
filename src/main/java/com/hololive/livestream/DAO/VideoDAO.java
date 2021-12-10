package com.hololive.livestream.DAO;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.DTO.VideoVue;
import com.hololive.livestream.util.VideoSearch;

@Repository
public class VideoDAO {
	private static final String MAPPER = "VideoMapper";

	@Autowired
	SqlSessionTemplate template;

	public List<VideoVue> readAllInUpcoming() {
		return template.selectList(MAPPER + ".readAllInUpcoming");
	}
	
	public List<VideoVue> readAllInUpcomingIn1Hour() {
		return template.selectList(MAPPER + ".readAllInUpcomingIn1Hour");
	}
	
	public List<VideoVue> readAllInLive() {
		return template.selectList(MAPPER + ".readAllInLive");
	}
	
	public List<VideoVue> readAllInCompletedIn3Days() {
		return template.selectList(MAPPER + ".readAllInCompletedIn3Days");
	}
	
	public List<VideoVue> readAllInCompletedBetweenSomeday(String start, String end, int offset) {
		return template.selectList(MAPPER + ".readAllInCompletedBetweenSomeday", new VideoSearch(start, end, offset));
	}
	
	public int readAllInCompletedSize(String start, String end) {
		return template.selectOne(MAPPER + ".readAllInCompletedSize", new VideoSearch(start, end));
	}
	
	
	public void createUpcoming(VideoDTO videoDto) {
		VideoDTO upcoming = readUpcomingByVideoId(videoDto.getVideoId());
		if (upcoming == null)
			template.insert(MAPPER + ".createUpcoming", videoDto);
		else {
			System.out.println("\t\t 이미 존재합니다.");
			setUpcomingRefreshedTrueByVideoId(upcoming.getVideoId());
			if (!upcoming.getScheduledStartTime().equals(videoDto.getScheduledStartTime())) {
				updateScheduledStartTime(videoDto);
				System.out.println("\t\t 시작 시간이 변경되었습니다. (" + upcoming.getScheduledStartTime() + "->" + videoDto.getScheduledStartTime() + ")");
			}
		}
	}
	
	public void createLive(VideoDTO videoDto) {
		if (readLiveByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createLive", videoDto);
		else {
			System.out.println("\t\t 이미 존재합니다.");
			setLiveRefreshedTrueByVideoId(videoDto.getVideoId());
		}
	}
	
	public void createCompleted(VideoDTO videoDto) {
		if (readCompletedByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createCompleted", videoDto);
		else
			System.out.println("\t\t 이미 존재합니다.");
	}
	
	public List<MemberDTO> readAllMember() {
		return template.selectList(MAPPER + ".readAllMember");
	}
	
	public VideoDTO readUpcomingByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readUpcomingByVideoId", videoId);
	}
	
	public String readMemberNameByChannelId(String channelId) {
		return template.selectOne(MAPPER + ".readMemberNameByChannelId", channelId);
	}
	
	public List<VideoDTO> readAllInUpcomingNotRefreshed() {
		return template.selectList(MAPPER + ".readAllInUpcomingNotRefreshed");
	}
	
	public List<VideoDTO> readAllInLiveNotRefreshed() {
		return template.selectList(MAPPER + ".readAllInLiveNotRefreshed");
	}
	
	public void deleteUpcomingByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteUpcomingByVideoId", videoId);
	}
	
	public void deleteLiveByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteLiveByVideoId", videoId);
	}
	
	public void setUpcomingAllRefreshedFalse() {
		template.update(MAPPER + ".setUpcomingAllRefreshedFalse");
	}
	
	public void setLiveAllRefreshedFalse() {
		template.update(MAPPER + ".setLiveAllRefreshedFalse");
	}
	
	
	
	public void setUpcomingRefreshedTrueByVideoId(String videoId) {
		template.update(MAPPER + ".setUpcomingRefreshedTrueByVideoId", videoId);
	}
	
	public void setLiveRefreshedTrueByVideoId(String videoId) {
		template.update(MAPPER + ".setLiveRefreshedTrueByVideoId", videoId);
	}
	
	public VideoDTO readLiveByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readLiveByVideoId", videoId);
	}
	
	public VideoDTO readCompletedByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readCompletedByVideoId", videoId);
	}
	
	public void updateScheduledStartTime(VideoDTO videoDto) {
		template.update(MAPPER + ".updateScheduledStartTime", videoDto);
	}
	
	public void deleteCompletedByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteCompletedByVideoId", videoId);
	}
}
