package com.hololive.livestream.DAO;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hololive.livestream.DTO.APIDTO;
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.util.SearchForm;

@Repository
public class VideoDAO {
	private static final String MAPPER = "VideoMapper";

	@Autowired
	SqlSessionTemplate template;
	
	public void setUpcomingAllRefreshedFalse() {
		template.update(MAPPER + ".setUpcomingAllRefreshedFalse");
	}
	
	public void setUpcomingRefreshedTrueByVideoId(String videoId) {
		template.update(MAPPER + ".setUpcomingRefreshedTrueByVideoId", videoId);
	}
	
	public void setLiveAllRefreshedFalse() {
		template.update(MAPPER + ".setLiveAllRefreshedFalse");
	}
	
	public void setLiveRefreshedTrueByVideoId(String videoId) {
		template.update(MAPPER + ".setLiveRefreshedTrueByVideoId", videoId);
	}
	
	public List<MemberDTO> readAllMember() {
		return template.selectList(MAPPER + ".readAllMember");
	}
	
	public String readMemberNameByChannelId(String channelId) {
		return template.selectOne(MAPPER + ".readMemberNameByChannelId", channelId);
	}
	
	public List<MemberDTO> readNotInUpcomingOrLive() {
		return template.selectList(MAPPER + ".readNotInUpcomingOrLive");
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
	
	public VideoDTO readUpcomingByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readUpcomingByVideoId", videoId);
	}
	
	public List<VideoDTO> readAllInUpcomingIn1Hour() {
		return template.selectList(MAPPER + ".readAllInUpcomingIn1Hour");
	}
	
	public List<VideoDTO> readAllInUpcomingIn1HourWithTooltip() {
		return template.selectList(MAPPER + ".readAllInUpcomingIn1HourWithTooltip");
	}
	
	public List<VideoDTO> readAllInUpcoming() {
		return template.selectList(MAPPER + ".readAllInUpcoming");
	}
	
	public List<VideoDTO> readAllInUpcomingNotRefreshed() {
		return template.selectList(MAPPER + ".readAllInUpcomingNotRefreshed");
	}
	
	public void updateScheduledStartTime(VideoDTO videoDto) {
		template.update(MAPPER + ".updateScheduledStartTime", videoDto);
	}
	
	public void deleteUpcomingByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteUpcomingByVideoId", videoId);
	}
	
	public void createLive(VideoDTO videoDto) {
		if (readLiveByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createLive", videoDto);
		else {
			System.out.println("\t\t 이미 존재합니다.");
			setLiveRefreshedTrueByVideoId(videoDto.getVideoId());
		}
	}
	
	public VideoDTO readLiveByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readLiveByVideoId", videoId);
	}
	
	public List<VideoDTO> readAllInLive() {
		return template.selectList(MAPPER + ".readAllInLive");
	}
	
	public List<VideoDTO> readAllInLiveWithTooltip() {
		return template.selectList(MAPPER + ".readAllInLiveWithTooltip");
	}
	
	public List<VideoDTO> readAllInLiveNotRefreshed() {
		return template.selectList(MAPPER + ".readAllInLiveNotRefreshed");
	}
	
	public void deleteLiveByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteLiveByVideoId", videoId);
	}
	
	public void createCompleted(VideoDTO videoDto) {
		if (readCompletedByVideoId(videoDto.getVideoId()) == null)
			template.insert(MAPPER + ".createCompleted", videoDto);
		else
			System.out.println("\t\t 이미 존재합니다.");
	}
	
	public VideoDTO readCompletedByVideoId(String videoId) {
		return template.selectOne(MAPPER + ".readCompletedByVideoId", videoId);
	}
	
	public List<VideoDTO> readAllInCompleted() {
		return template.selectList(MAPPER + ".readAllInCompleted");
	}
	
	public List<VideoDTO> readAllInCompletedIn1Days() {
		return template.selectList(MAPPER + ".readAllInCompletedIn1Days");
	}
	
	public List<VideoDTO> readAllInCompletedIn3Days() {
		return template.selectList(MAPPER + ".readAllInCompletedIn3Days");
	}
	
	public List<VideoDTO> readAllInCompletedBetweenSomeday(String start, String end) {
		return template.selectList(MAPPER + ".readAllInCompletedBetweenSomeday", new SearchForm(start, end));
	}
	
	public void deleteCompletedByVideoId(String videoId) {
		template.delete(MAPPER + ".deleteCompletedByVideoId", videoId);
	}
	
	public APIDTO readMinQuotasAPIKey() {
		return template.selectOne(MAPPER + ".readMinQuotasAPIKey");
	}
	
	public void increaseQuotas100(String apiKey) {
		template.update(MAPPER + ".increaseQuotas100", apiKey);
	}
	
	public void increaseQuotas1(String apiKey) {
		template.update(MAPPER + ".increaseQuotas1", apiKey);
	}
	
	public void resetQuota() {
		template.update(MAPPER + ".resetQuota");
	}
}
