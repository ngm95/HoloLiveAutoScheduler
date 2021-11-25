package com.hololive.livestream.Quartz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.hololive.livestream.DAO.VideoDAO;

/**
 * @author ngm95
 *
 * API Key의 할당량을 초기화하는 QuartzJobBean
 * 매일 17시 정각에 실행함
 * 
 * 
 */
@Component
public class ResetQuota extends QuartzJobBean {
	
	@Autowired
	private VideoDAO videoDao;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		System.out.println(format.format(Calendar.getInstance().getTime()) + " : resetQuota");
		
		videoDao.resetQuota();
	}

}
