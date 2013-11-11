/**
 * project name: myNews
 * created at 2013-3-7 - 上午9:30:37
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.uestc.news.service.SynService;

/**
 * @author yuer
 * 
 */
public class SpringCronJobSyn extends SpringCronJob {
	private static Logger logger = LoggerFactory.getLogger(SpringCronJobSyn.class);

	@Autowired
	private SynService synService;

	@PostConstruct
	public void start() {
		Validate.notBlank(cronExpression);

		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("SpringCronJobForQSHP");
		threadPoolTaskScheduler.initialize();
		threadPoolTaskScheduler.schedule(this, new CronTrigger(cronExpression));

		logger.info("登录河畔程序开始了，自动发帖到微博程序开始，crontab");
	}

	@Override
	public void run() {
		try {
			synService.posting();
		} catch (UnknownHostException e) {
			logger.error("出错了----UnknownHostException");
		} catch (SocketTimeoutException e) {
			logger.error("出错了----SocketTimeoutException");
		} catch(HttpHostConnectException e){
			logger.error("出错了----HttpHostConnectException");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
