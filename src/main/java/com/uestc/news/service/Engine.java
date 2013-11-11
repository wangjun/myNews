package com.uestc.news.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.uestc.news.entity.Channel;
import com.uestc.news.entity.News;
import com.uestc.news.utils.MD5;

@Component
public class Engine extends Thread {

	private static Logger logger = LoggerFactory.getLogger(Engine.class);

	private static final int CONNECTION_POOL_SIZE = 10;
	private static final int TIMEOUT_SECONDS = 20;

	@Autowired
	private NewsService newsService;

	@Autowired
	HexunNewsEngine hxNewsEngine;
	
	@Autowired
	HuxiuService huxiuService;

	public void parseRss(String rss, String source) {
		Channel channel = new Channel();
		List<News> newsList = new ArrayList<News>();
		try {
			URL url = new URL(rss);
			XmlReader reader = new XmlReader(url);// 读取Rss源
			logger.info("[url="+rss+"][Rss源的编码格式为：" + reader.getEncoding()+"]");
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

				news.setDescription(entry.getDescription().getValue());

				if (entry.getContents() != null && entry.getContents().size() > 0) {
					SyndContent c = (SyndContent) entry.getContents().get(0);
					news.setDescription(c.getValue());
				}
				news.setPubDate(entry.getPublishedDate());
				news.setClawTime(new Date());

				// 以下是Rss源可先的几个部分
				news.setAuthor(entry.getAuthor());
				news.setSource(source);

				// 此标题所属的范畴
				// List categoryList = entry.getCategories();
				// if (categoryList != null) {
				// for (int m = 0; m < categoryList.size(); m++) {
				// SyndCategory category = (SyndCategory) categoryList.get(m);
				// // System.out.println("此标题所属的范畴：" + category.getName());
				// }
				// }
				news.setSynQshp(false);
				news.setSynWeibo(false);
				newsList.add(news);
			}
			channel.setItem(newsList);
			newsService.saveNewsByChannel(channel);
			logger.info("正在抓取" + channel.getTitle() + channel.getLink() + new Date());
		} catch (Exception e) {
			logger.info("抓取地址：" + rss + "来源：" + source, e);
		}
	}

	public void enginStart() {
		this.getTianXiaWangShang("http://i.wshang.com/Post/Default/Rss.html", "天下网商");
		
		this.parseRss("http://www.36kr.com/feed", "36Kr");
		this.parseRss("http://feed.williamlong.info/", "williamlong");
		this.parseRss("http://feeds.geekpark.net/", "geekpark");
		this.getZhihu("http://www.zhihu.com/reader/json/1", "知乎");
		this.parseRss("http://tech2ipo.com/feed", "TECH2IPO创见");

//		this.parseRss("http://feed.feedsky.com/programmer", "程序员杂志");
		this.parseRss("http://feed.yixieshi.com/", "互联网的一些事");
		this.parseRss("http://www.pingwest.com/feed/", "pingwest");
		this.parseRss("http://www.tmtpost.com/?feed=rss2", "钛媒体");
		this.parseRss("http://songshuhui.net/feed", "科学松鼠会");
		this.parseRss("http://www.ruanyifeng.com/blog/atom.xml","阮一峰");
		

		// this.parseRss("http://www.leiphone.com/feed", "雷锋网");
		this.parseRss("http://www.ifanr.com/feed", "爱范儿");

		hxNewsEngine.getHXNews("http://rss.hexun.com/FeedItems.aspx?feedID=126095", "hexun");// 银行
		hxNewsEngine.getHXNews("http://rss.hexun.com/FeedItems.aspx?feedID=126582", "hexun");// 保险
		hxNewsEngine.getHXNews("http://rss.hexun.com/FeedItems.aspx?feedID=127463", "hexun");// 理财
		
		huxiuService.getHuxiu();
	}

	public void getZhihu(String contentUrl, String source) {
		/**
		 * 使用HttpClient取得内容.
		 */
		HttpClient httpClient = null;
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(CONNECTION_POOL_SIZE);
		httpClient = new DefaultHttpClient(cm);

		// set timeout
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_SECONDS * 1000);

		// 获取内容
		HttpEntity entity = null;
		HttpGet httpGet = new HttpGet(contentUrl);
		try {
			HttpResponse remoteResponse = httpClient.execute(httpGet);
			entity = remoteResponse.getEntity();
		} catch (Exception e) {
			logger.error("fetch remote content" + contentUrl + "  error", e);
			httpGet.abort();
			return;
		}

		// 404返回
		if (entity == null) {
			return;
		}
		// 输出内容
		InputStream input;
		try {
			input = entity.getContent();
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = input.read(b)) != -1;) {
				out.append(new String(b, 0, n, "utf-8"));
			}
			String ret = out.toString();

			logger.info(ret);

			Channel channel = new Channel();
			List<News> newsList = new ArrayList<News>();
			JSONArray jsonArray = new JSONArray(ret);
			for (int i = 0; i < jsonArray.length(); i++) {
				News news = new News();
				JSONArray jsonArray2 = jsonArray.getJSONArray(i);
				String auth = jsonArray2.getString(6).equals("0") ? "匿名用户" : new JSONArray(jsonArray2.getString(6)).getString(0);
				news.setAuthor(auth);
				news.setCatalog(null);
				news.setClawTime(new Date());
				news.setDescription(jsonArray2.getString(2));
				news.setLink("http://www.zhihu.com/question/" + new JSONArray(jsonArray2.getString(7)).getString(3));
				MD5 getMD5 = new MD5();
				news.setHash(getMD5.Md5Encode(news.getLink()));

				Long timestamp = Long.parseLong(jsonArray2.getString(4)) * 1000;
				news.setPubDate(new Date(timestamp));

				news.setTitle(new JSONArray(jsonArray2.getString(7)).getString(1));
				news.setSource(source);

				news.setSynQshp(false);
				news.setSynWeibo(false);

				newsList.add(news);

				/*
				 * for (int j = 0; j < jsonArray2.length() - 1; j++) { String
				 * objArray = jsonArray2.getString(j); logger.info(j +
				 * "--------" + objArray); }
				 * logger.info("================================");
				 */
			}
			channel.setItem(newsList);
			newsService.saveNewsByChannel(channel);
		} catch (IllegalStateException e) {
			logger.info("IllegalStateException  ", e);
		} catch (IOException e) {
			logger.info("IOException  ", e);
		} catch (JSONException e) {
			logger.info("JSONException  ", e);
		}
	}

	public void getTianXiaWangShang(String rss, String source) {
		// contentUrl = "http://i.wshang.com/Post/Default/Index/pid/32761.html";
		Channel channel = new Channel();
		List<News> newsList = new ArrayList<News>();
		try {
			URL url = new URL(rss);
			XmlReader reader = new XmlReader(url);// 读取Rss源
			logger.info("Rss源的编码格式为：" + reader.getEncoding());
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

				String contentUrl = entry.getLink();
				
				News news = new News();
				news.setTitle(entry.getTitle());
				news.setLink(entry.getLink());
				MD5 getMD5 = new MD5();
				news.setHash(getMD5.Md5Encode(entry.getLink()));
				
				Document doc = Jsoup.connect(contentUrl)
						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.57 Safari/537.36").get();
				logger.info("正在抓取" + contentUrl);
				Element e = doc.getElementsByClass("detailMain").get(0).getElementsByClass("articleBox").get(0);
				String title = e.getElementsByTag("h1").html();
				String timeAndAuthor = e.getElementsByClass("shareBox").get(0).getElementsByTag("span").get(0).getElementsByTag("b").get(0).html();
				String[] tas = timeAndAuthor.split("\\|");
				String time = tas[0];
				String author = Jsoup.parse(timeAndAuthor).getElementsByTag("a").html();
				logger.info("发布时间" + time + "标题" + title + "作者：" + author);
				String content = e.getElementsByClass("textBox").get(0).html();
				Whitelist whitelist = new Whitelist().addTags("a", "b", "br", "p", "strong").addAttributes("a", "href").addAttributes("img", "src")
						.addProtocols("a", "href", "ftp", "http", "https", "mailto");
				content = Jsoup.clean(content, whitelist);

				if (content.length() < 30) {
					logger.info("可能没有的" + contentUrl);
					return;
				}

				news.setAuthor(author);
				news.setCatalog(null);
				news.setClawTime(new Date());
				news.setDescription(content);
				news.setLink(contentUrl);

				String[] pattern = new String[] { "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss" };

				Date ttime = DateUtils.parseDate(time, pattern);
				ttime.setHours(0);
				news.setPubDate(ttime);

				news.setTitle(title);
				news.setSource(source);
				news.setSynQshp(false);
				news.setSynWeibo(false);

				newsList.add(news);
			}
			channel.setItem(newsList);
			newsService.saveNewsByChannel(channel);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Engine engine = new Engine();
		// engine.parseRss("http://www.36kr.com/feed","36kr");
		// engine.parseRss("http://tech2ipo.com/feed", "a");
		// engine.getZhihu("http://www.zhihu.com/reader/json/1", "zhihu");
		engine.getTianXiaWangShang("http://i.wshang.com/Post/Default/Rss.html", "天下网商");
	}

}
