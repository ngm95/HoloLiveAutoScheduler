package com.hololive.livestream.DAO;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;

@Repository
public class VideoDAO {
	private static final String MAPPER = "VideoMapper";

	@Autowired
	SqlSessionTemplate template;
	
	public List<MemberDTO> readNotInUpcomingOrLive() {
		return template.selectList(MAPPER + ".readNotInUpcomingOrLive");
	}
	
	public void createUpcoming(VideoDTO videoDto) {
		if (readUpcomingByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createUpcoming", videoDto);
	}
	
	public VideoDTO readUpcomingByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readUpcomingByVideoId", videoId);
	}
	
	public List<VideoDTO> readAllInUpcoming() {
		return template.selectList(MAPPER + ".readAllInUpcoming");
	}
	
	public void deleteUpcomingByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteUpcomingByVideoId", videoId);
	}
	
	public void createLive(VideoDTO videoDto) {
		if (readLiveByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createLive", videoDto);
	}
	
	public VideoDTO readLiveByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readLiveByVideoId", videoId);
	}
	
	public List<VideoDTO> readAllInLive() {
		return template.selectList(MAPPER + ".readAllInLive");
	}
	
	public void deleteLiveByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteLiveByVideoId", videoId);
	}
	
	public void createCompleted(VideoDTO videoDto) {
		if (readCompletedByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createCompleted", videoDto);
	}
	
	public VideoDTO readCompletedByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readCompletedByVideoId", videoId);
	}
	
	public List<VideoDTO> readAllInCompleted() {
		return template.selectList(MAPPER + ".readAllInCompleted");
	}
	
	public List<VideoDTO> readAllInCompletedIn2Days() {
		return template.selectList(MAPPER + ".readAllInCompletedIn2Days");
	}
	
	public void deleteCompletedByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteCompletedByVideoId", videoId);
	}
}
