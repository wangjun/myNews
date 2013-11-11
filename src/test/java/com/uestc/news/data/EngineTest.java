package com.uestc.news.data;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uestc.news.service.Engine;

public class EngineTest {
	private Logger logger = LoggerFactory.getLogger(EngineTest.class);

	@Test
	public void testEngine() {
		Engine engine = new Engine();
		// engine.parseRss("http://www.36kr.com/feed","36kr");
		// engine.parseRss("http://tech2ipo.com/feed", "a");
		// engine.getZhihu("http://www.zhihu.com/reader/json/1", "zhihu");
		String contentUrl = "http://i.wshang.com/Post/Default/Index/pid/";
		for (int i = 30000; i < 32761; i++) {
			String url = contentUrl + i + ".html";
			engine.getTianXiaWangShang(url, "天下网商");
			logger.info("---------------------------------------------");
		}
	}
}
