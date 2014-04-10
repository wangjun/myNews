/**
 * project name: myNews
 * created at 2013-3-2 - 下午6:23:29
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uestc.news.entity.News;
import com.uestc.news.utils.Config;
import com.uestc.news.utils.HtmlTagUtils;
import com.uestc.news.utils.HttpUtils;

/**
 * @author yuer
 * 
 *         适用于老版的清水河畔登录
 * 
 */
@Component
public class BBSLoginService {
	private static Logger logger = LoggerFactory.getLogger(BBSLoginService.class);

	@Autowired
	private NewsService newsService;

	private String login(HttpUtils httpUtils) throws Exception {
		String v = isLogin(httpUtils);
		if (v != null) {
			return v;
		}

		String url = Config.getLoginUrl();
		String result = httpUtils.get(url);
		String rand = getByReg(result, "rand[A-Za-z0-9_= ']+").substring(5);
		if (rand != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", Config.getProperties("username")));
			nvps.add(new BasicNameValuePair("password", Config.getProperties("qshpass")));
			result = httpUtils.postQSHP(Config.getProperties("adminPhp") + rand, nvps);// 有一个302

			String comfirmUrl = getByReg(result, "http://bbs.qshpan.com/login.php[?A-Za-z0-9_=&.]+");
			result = httpUtils.get(comfirmUrl);

			String aaa = httpUtils.get(Config.getModelUrl());

			String verify = getByReg(aaa, "verifyhash[A-Za-z0-9_= ']+");
			verify = verify.substring(14, 22);
			logger.info("登录成功verify  code is === " + verify);
			return verify;
		}
		return null;
	}

	private String isLogin(HttpUtils httpUtils) throws Exception {
		String verify = null;
		String aaa = httpUtils.get(Config.getModelUrl());
		verify = getByReg(aaa, "verifyhash[A-Za-z0-9_= ']+");
		verify = verify.substring(14, 22);// verifyhash = 'e9f78bb2'
		String name = getByReg(aaa, Config.getProperties("username"));
		if (name == null || name.length() == 0) {
			return null;
		}
		logger.info("已经登录过了 ： verify code is === " + verify + "name = " + name);
		return verify;
	}

	/**
	 * 发帖
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public void posting(News news) throws IOException, Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(news.getTitle());
		stringBuilder.append(" --知乎");
		stringBuilder.append(" [");
		stringBuilder.append(new Date().toLocaleString());
		stringBuilder.append("]");
		String title = stringBuilder.toString();
		if (title.length() > 50) {
			title = title.substring(0, 49);
		}
		StringBuilder content = new StringBuilder();
		content.append("[by 知乎自动发帖]\n\nNow：" + new Date() + "\n\n新闻自动转发\n\n");
		news.setSynQshp(true);
		newsService.saveNews(news);
		content = content.append("[hr][b][size=4]" + news.getTitle() + "[/size][/b]\n\n[size=3]原文[/size][url=" + news.getLink() + "][size=3]" + news.getLink()
				+ "[/size][/url]\n[font=arial][color=#333333][backcolor=#f2fcff]发布时间: " + news.getPubDate()
				+ "[/backcolor][/color][/font]\n\n[color=#333333][size=3]" + HtmlTagUtils.getonerow(news.getDescription()) + "[/size][/color]\n\n");

		HttpUtils httpUtils = new HttpUtils();
		String verify = login(httpUtils);
		// 发帖程序开始
		List<NameValuePair> postsNvps = new ArrayList<NameValuePair>();
		postsNvps.add(new BasicNameValuePair("verify", verify));
		postsNvps.add(new BasicNameValuePair("atc_title", title));
		postsNvps.add(new BasicNameValuePair("atc_content", content.toString()));

		postsNvps.add(new BasicNameValuePair("formhash", "f78510c5"));
		postsNvps.add(new BasicNameValuePair("posttime", "1389583185"));
		postsNvps.add(new BasicNameValuePair("wysiwyg", "1"));
		postsNvps.add(new BasicNameValuePair("typeid", "761"));
		postsNvps.add(new BasicNameValuePair("subject", "ccccccccccccccccccc"));
		postsNvps.add(new BasicNameValuePair("message", "dddddddddddddddddd%0D%0A"));
		postsNvps.add(new BasicNameValuePair("price", ""));
		postsNvps.add(new BasicNameValuePair("allownoticeauthor", "1"));
		postsNvps.add(new BasicNameValuePair("usesig", "1"));
		postsNvps.add(new BasicNameValuePair("save", ""));
		postsNvps.add(new BasicNameValuePair("uploadalbum", "-2"));
		postsNvps.add(new BasicNameValuePair("newalbum", "%E8%AF%B7%E8%BE%93%E5%85%A5%E7%9B%B8%E5%86%8C%E5%90%8D%E7%A7%B0"));

		httpUtils.post(Config.getPostUrl(), postsNvps, "gb2312");
		logger.info("发布帖子成功,发布时间：" + new Date() + " title=" + title);
	}

	private static String getByReg(String source, String regex) {
		String ret = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);
		while (m.find()) {
			ret = m.group();
		}
		return ret;
	}

}