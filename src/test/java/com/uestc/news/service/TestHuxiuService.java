package com.uestc.news.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.uestc.news.utils.HttpUtils;

public class TestHuxiuService {
	@Test
	public void testHuxiu() {
		HuxiuService huxiu = new HuxiuService();
		huxiu.getHuxiu();
	}

	@Test
	public void testJsoup() throws Exception {
		HttpUtils httpUtils = new HttpUtils();
		String htmlString = httpUtils.get("http://feed.yixieshi.com");
		// String htmlString = httpUtils.get("http://www.pingwest.com/feed/");
		System.out.println(httpUtils._getCharSetByBody(htmlString));
		SyndFeedInput input = new SyndFeedInput();

		InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
		InputSource is = new InputSource(inputStream);

		SyndFeed feed = input.build(is);

		// 得到Rss新闻中子项列表
		List entries = feed.getEntries();
		for (int i = 0; i < feed.getEntries().size(); i++) {
			SyndEntry entry = (SyndEntry) entries.get(i);
			System.out.println(entry.getTitle());
		}
	}
}
