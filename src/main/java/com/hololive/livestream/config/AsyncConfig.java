package com.hololive.livestream.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
	private static int TASK_CORE_POOL_SIZE = 1;
	private static int TASK_MAX_POOL_SIZE = 1;
	private static int TAST_QUEUE_CAPACITY = 0;
	private final String EXECUTOR_BEAN_NAME = "executor";
	
	@Bean(name="executor")
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(TASK_CORE_POOL_SIZE);
		executor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
		executor.setQueueCapacity(TAST_QUEUE_CAPACITY);
		executor.setBeanName(EXECUTOR_BEAN_NAME);
		executor.setWaitForTasksToCompleteOnShutdown(false);
		executor.initialize();
		return executor;
	}
}