package com.hololive.livestream.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hololive.livestream.Quartz.CheckChannel;
import com.hololive.livestream.Quartz.CheckLive;
import com.hololive.livestream.Quartz.CheckUpcoming;
import com.hololive.livestream.Quartz.ResetQuota;

@Configuration
public class QuartzConfig {
	@Autowired
	private Scheduler scheduler;

	/**
	 * 프로그램이 시작되면 특정 클래스를 Cron 표기법에 표현된 시간마다 실행함
	 */
	@PostConstruct
	public void start() {
		try {
			JobDetail checkChannel = buildJobDetail(CheckChannel.class, "checkChannel", "", new HashMap());
			if (scheduler.checkExists(checkChannel.getKey()))
				scheduler.deleteJob(checkChannel.getKey());
			
			JobDetail checkUpcoming = buildJobDetail(CheckUpcoming.class, "checkUpcoming", "", new HashMap());
			if (scheduler.checkExists(checkUpcoming.getKey()))
				scheduler.deleteJob(checkUpcoming.getKey());
			
			JobDetail checkLive = buildJobDetail(CheckLive.class, "checkLive", "", new HashMap());
			if (scheduler.checkExists(checkLive.getKey()))
				scheduler.deleteJob(checkLive.getKey());
			
			JobDetail resetQuota = buildJobDetail(ResetQuota.class, "resetQuota", "", new HashMap());
			if (scheduler.checkExists(resetQuota.getKey()))
				scheduler.deleteJob(resetQuota.getKey());
			
			
			scheduler.scheduleJob(checkChannel, buildCronJobTrigger("0 0 * * * ?"));	// 모든 시각 0분 0초마다 실행
			System.out.println("checkChannel");
			scheduler.scheduleJob(checkUpcoming, buildCronJobTrigger("0 0/5 * * * ?"));	// 모든 시각 0분 0초에 시작해서 5분 간격으로 실행
			System.out.println("checkUpcoming");
			scheduler.scheduleJob(checkLive, buildCronJobTrigger("0 0/20 * * * ?"));	// 모든 시각 0분 0초에 시작해서 20분 간격으로 실행
			System.out.println("checkLive");
			scheduler.scheduleJob(resetQuota, buildCronJobTrigger("0 0 17 * * ?"));		// 매일 오후 17시 0분 0초에 실행
			System.out.println("resetQuota");
		} catch (SchedulerException se) {
			System.out.println("error");
		}
	}

	public Trigger buildCronJobTrigger(String scheduleExp) {
		return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
	}

	public Trigger buildSimpleJobTrigger(Integer hour) {
		return TriggerBuilder.newTrigger()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(hour)).build();
	}

	public JobDetail buildJobDetail(Class job, String name, String desc, Map params) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);
		return JobBuilder.newJob(job).withIdentity(name).withDescription(desc).usingJobData(jobDataMap).build();
	}
}
