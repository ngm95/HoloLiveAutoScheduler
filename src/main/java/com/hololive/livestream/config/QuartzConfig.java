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
			
			scheduler.scheduleJob(checkChannel, buildCronJobTrigger("0 0/2 * * * ?", 15));			// 모든 시각 0분 20초마다 실행
			System.out.println("checkChannel");
			
		} catch (SchedulerException se) {
			System.out.println("error");
		}
	}

	public Trigger buildCronJobTrigger(String scheduleExp, int priority) {
		return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).withPriority(priority).build();
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
