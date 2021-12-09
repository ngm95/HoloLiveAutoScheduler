package com.hololive.livestream.Quartz;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hololive.livestream.DTO.MemberDTO;
import com.hololive.livestream.DTO.VideoAPI;
import com.hololive.livestream.DTO.VideoDTO;
import com.hololive.livestream.Service.VideoService;

/**
 * @author ngm95
 *
 *         정해진 유튜브 채널을 체크해서 새로 예약된 영상이 있는지 검사하는 QuartzJobBean 매시 0분 0초에 실행하므로
 *         하루에 총 24번 실행됨
 * 
 *         1시간 이전부터 현재 시간까지 예약된 동영상이 있으면 영상들을 모두 Upcoming 테이블에 저장한다.
 */
@Component
public class CollectCompletedVideos extends QuartzJobBean {

	@Autowired
	private VideoService videoServ;

	ObjectMapper objectMapper = new ObjectMapper();
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	
	private int test = 0;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		test();
	}
	
	private void test() {
		System.out.println(test++);
	}
}
