package com.uestc.news.service;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uestc.news.entity.Channel;
import com.uestc.news.entity.News;
import com.uestc.news.utils.JPDateUtils;
import com.uestc.news.utils.MD5;

@Component
public class HexunNewsEngine {
	private static Logger logger = LoggerFactory.getLogger(HexunNewsEngine.class);
	@Autowired
	private NewsService newsService;

	public void getHXNews(String rssUrl, String source) {
		Channel channel = new Channel();
		List<News> newsList = new ArrayList<News>();

		Document doc;
		try {
			doc = Jsoup.connect(rssUrl).get();
			Element element = doc.getElementById("Form1");
			String catalog = doc.getElementsByClass("sheisse").get(0).html();// 获取rss分类信息
			catalog = Jsoup.clean(catalog, Whitelist.none());
			Elements elementList = element.getElementsByAttributeValue("style", "border-top: none;border-bottom:1px dashed;");
			for (Element tempElement : elementList) {
				News news = new News();
				Elements tem = tempElement.getElementsByTag("a");// get rss list
																	// link info
				String href = tem.get(0).attr("href");// get rss list 's item
														// link addres
				String title = tem.get(0).html();// get rss list's item link
													// content
				doc = Jsoup.connect("http://rss.hexun.com" + href).get();
				String frame = doc.getElementById("bottomFrame").attr("src");// Frame框架的中间正文部分链接
				String link = "http://rss.hexun.com" + frame;
				link = getFrameUrl(Jsoup.connect(link).get().html());
				logger.info("即将抓取页面：" + link);
				doc = Jsoup.connect(link).get();
				String content = doc.getElementById("artibody").html();
				Element desc = doc.getElementById("artibodyDesc");
				content = Jsoup.clean(content, Whitelist.basic());
				Elements spanList = desc.getElementsByTag("span");
				// 2012年06月13日23:49 来源：银行家
				// 作者：张光伟;2013年04月09日03:06
				// 来源：证券时报 ;有时候是没有作者信息的
				if (spanList.html().contains("作者")) {
					news.setAuthor(spanList.get(0).getElementsByTag("font").get(0).html());
				}
				Element titleDesc = desc.getElementsByTag("a").get(0);
				String pubTime = spanList.get(1).html();
				if (news.getAuthor() == null || news.getAuthor().length() == 0) {
					news.setAuthor(titleDesc.html());
				}
				news.setCatalog(catalog);
				news.setClawTime(new Date());
				news.setDescription(content);
				news.setLink(link);
				MD5 getMD5 = new MD5();
				news.setHash(getMD5.Md5Encode(news.getLink()));
				news.setPubDate(JPDateUtils.toEng(pubTime));
				news.setSource(source);
				news.setSynQshp(false);
				news.setSynWeibo(false);
				news.setTitle(title);

				logger.info("文章发布作者:" + news.getAuthor());
				logger.info("文章发布时间：" + JPDateUtils.toEng(pubTime));
				logger.info("文章中的来源：" + titleDesc.html());
				logger.info("文章来源链接：" + titleDesc.attr("href"));
				logger.info("文章原文链接：" + link);
				logger.info("文章原文Hash：" + news.getHash());
				logger.info("文章原文分类：" + news.getCatalog());

				newsList.add(news);
				// logger.info(news.toString());
				logger.info("页面抓取完毕：" + link);
				logger.info("=======================================================================================");
			}
			channel.setItem(newsList);
			// newsService.saveNewsByChannel(channel);
		} catch (SocketTimeoutException e) {
			logger.info("哦哦，出错了SocketTimeoutException", e);
		} catch (IOException e) {
			logger.info("哦哦，出错了", e);
		} catch (IndexOutOfBoundsException e) {
			logger.info("哦哦，里面数据有越界", e);
		} catch (Exception e) {
			logger.info("哦哦，有异常啊Exception", e);
		}
	}

	public static void main(String[] args) throws IOException {
		// HexunNewsEngine hx = new HexunNewsEngine();
		// hx.getHXNews("http://rss.hexun.com/FeedItems.aspx?feedID=126095",
		// "hexun");
		File input = new File("D:/workspace/myNews/html.txt");
		Document doc = Jsoup.parse(input, "gb2312", "http://www.oschina.net/");

		logger.info(doc.html());
		Whitelist w = new Whitelist().addTags("a", "b", "br", "p", "strong").addAttributes("a", "href").addAttributes("img", "src")
				.addProtocols("a", "href", "ftp", "http", "https", "mailto");
		logger.info(Jsoup.clean(doc.html(), w));
	}

	/**
	 * 获取最终链接到http://bank.hexun.com/2013-04-09/152970002.html?from=rss的地址
	 * 
	 * @param html
	 * @return
	 */
	private String getFrameUrl(String html) {
		Pattern p = Pattern.compile("src='[\\w:/.\\-=?]+");
		Matcher m = p.matcher(html);
		String temp = "";
		while (m.find()) {
			temp = m.group();
		}
		return temp.substring(5);
	}
}
