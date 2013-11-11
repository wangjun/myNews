/**
 * project name: myNews
 * created at 2013-3-8 - 下午6:41:38
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
import com.uestc.news.utils.HttpUtils;

/**
 * @author yuer
 * 
 */
@Component
public class WeiboService {
	private static Logger logger = LoggerFactory.getLogger(WeiboService.class);

	@Autowired
	private NewsService newsService;

	public void post(News news) {
		try {
			HttpUtils httpclient = new HttpUtils();
			String doc = httpclient.get(Config.getWeiboLoginUrl());

			String passName = this.getByReg(doc, "password_[0-9]+");
			String rand = this.getByReg(doc, "rand[A-Za-z0-9_= ']+").substring(5);
			String vk = this.getByReg(doc, "vk\" value[A-Za-z0-9_= '\"]+");
			vk = vk.substring(11, vk.length() - 2);
			logger.info("微博账号登陆：rand=" + rand);
			logger.info("微博账号登陆：vk=" + vk);

			String url = "http://login.weibo.cn/login/?rand=" + rand
					+ "&backURL=http%3A%2F%2Fweibo.cn%2F%3Fs2w%3Dlogin&backTitle=%E6%96%B0%E6%B5%AA%E5%BE%AE%E5%8D%9A&vt=4&revalid=2&ns=1";

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("mobile", Config.getWeiboName()));
			qparams.add(new BasicNameValuePair(passName, Config.getWeiboPsw()));
			qparams.add(new BasicNameValuePair("remember", "on"));
			qparams.add(new BasicNameValuePair("backURL", "http%3A%2F%2Fweibo.cn%2F%3Fs2w%3Dlogin"));
			qparams.add(new BasicNameValuePair("backTitle", "新浪微博"));
			qparams.add(new BasicNameValuePair("vk", vk));
			qparams.add(new BasicNameValuePair("submit", "登录"));

			String login = httpclient.postWeibo(url, qparams);
			String jumpUrl = this.getByReg(login, "http://weibo.cn/[A-Za-z0-9_= '?&;]+");
			String home = httpclient.get(jumpUrl);
			if (getByReg(home, "yuer_cl").length() > 0) {
				logger.info("微博登陆成功" + new Date());
			}

			String gisd = this.getByReg(home, "gsid[A-Za-z0-9_=]+").substring(5);
			String st = this.getByReg(home, "st=[0-9a-zA-Z]+").substring(3);
			url = "http://weibo.cn/mblog/sendmblog?vt=4&gsid=" + gisd + "&st=" + st;
			qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("content", news.getTitle() + "[嘻嘻]来至"+news.getSource()+"[嘻嘻]"+news.getLink()+"[嘻嘻]via yuercl自动微博 & yuercl'Reader http://yuercl.sinaapp.com "));
			qparams.add(new BasicNameValuePair("rl", "0"));

			String post = httpclient.postWeibo(url, qparams);
			if (getByReg(post, "发布成功").length() > 0) {
				logger.info("微博发布成功");
			}

		} catch (IOException e) {
			logger.error("发布微博错误，IOException", e);
		} catch (Exception e) {
			logger.error("发布微博错误，Exception", e);
		}
		news.setSynWeibo(true);
		newsService.saveNews(news);
	}

	private String getByReg(String source, String regex) {
		String ret = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);
		while (m.find()) {
			ret = m.group();
		}
		return ret;
	}
}
