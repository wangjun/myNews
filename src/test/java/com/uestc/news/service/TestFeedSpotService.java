package com.uestc.news.service;

import org.junit.Test;

public class TestFeedSpotService {

	@Test
	public void testFeedsportal() {
		Engine e = new Engine();
		e.parseRss("http://tech2ipo.com/feed", "TECH2IPO创见");
	}
}
