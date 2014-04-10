/**
 * project name: myNews
 * created at 2013-3-7 - 上午9:30:37
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.uestc.news.service.SynService;

public class SpringCronJobSyn implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SpringCronJobSyn.class);

	public String cronExpression;

	public int shutdownTimeout = Integer.MAX_VALUE;

	public ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	private SynService synService;

	@PreDestroy
	public void stop() {
		ScheduledExecutorService scheduledExecutorService = threadPoolTaskScheduler.getScheduledExecutor();
		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.SECONDS);
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * 设置normalShutdown的等待时间,单位秒.
	 */
	public void setShutdownTimeout(int shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}

	@PostConstruct
	public void start() {
		Validate.notBlank(cronExpression);
		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("SpringCronJobForQSHP");
		threadPoolTaskScheduler.initialize();
		threadPoolTaskScheduler.schedule(this, new CronTrigger(cronExpression));
		logger.info("[登录河畔程序开始了，自动发帖到微博程序开始]");
	}

	@Override
	public void run() {
		synService.posting();
	}
}
