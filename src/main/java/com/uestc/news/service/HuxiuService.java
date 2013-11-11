package com.uestc.news.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.uestc.news.entity.Channel;
import com.uestc.news.entity.News;
import com.uestc.news.utils.MD5;

@Component
public class HuxiuService {
	private static Logger logger = LoggerFactory.getLogger(HuxiuService.class);

	@Autowired
	private NewsService newsService;

	public void getHuxiu() {
		this._parseRss("http://www.huxiu.com/rss/0.xml", "虎嗅网");
	}

	private void _parseRss(String rss, String source) {
		Channel channel = new Channel();
		List<News> newsList = new ArrayList<News>();
		try {
			URL url = new URL(rss);
			XmlReader reader = new XmlReader(url);// 读取Rss源
			logger.info("[url=" + rss + "][Rss源的编码格式为：" + reader.getEncoding() + "]");
			logger.info("正在抓取" + channel.getTitle() + channel.getLink() + new Date());
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(reader);// 得到SyndFeed对象，即得到Rss源里的所有信息

			channel.setDescription(feed.getDescription());
			channel.setLanguage(feed.getLanguage());
			channel.setLink(feed.getLink());
			channel.setTitle(feed.getTitle());

			// 得到Rss新闻中子项列表
			List entries = feed.getEntries();
			for (int i = 0; i < feed.getEntries().size(); i++) {
				SyndEntry entry = (SyndEntry) entries.get(i);
				// 标题、连接地址、标题简介、时间是一个Rss源项最基本的组成部分

				News news = new News();
				news.setTitle(entry.getTitle());
				news.setLink(entry.getLink());
				MD5 getMD5 = new MD5();
				news.setHash(getMD5.Md5Encode(entry.getLink()));

				News tempNews = _getContent(news.getLink());

				news.setDescription(tempNews.getDescription());

				news.setPubDate(entry.getPublishedDate());
				news.setClawTime(new Date());

				// 以下是Rss源可先的几个部分
				// news.setAuthor(entry.getAuthor());
				news.setAuthor(tempNews.getAuthor());
				news.setSource(source);

				news.setSynQshp(false);
				news.setSynWeibo(false);
				newsList.add(news);
			}
			channel.setItem(newsList);
			newsService.saveNewsByChannel(channel);
			logger.info("抓取完毕");
		} catch (Exception e) {
			logger.info("出错啦，抓取地址：" + rss, e);
		}
	}

	private News _getContent(String contentUrl) {
		News news = new News();
		try {
			logger.info("正在抓取" + contentUrl);
			Document doc = Jsoup.connect(contentUrl)
					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.57 Safari/537.36").get();

			String content = doc.getElementById("neirong_box").html();
			String author = doc.getElementsByClass("recommenders").get(0).text();
			news.setDescription(content);
			news.setAuthor(author);
			return news;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("有错误", e);
		}
		return null;
	}

}
