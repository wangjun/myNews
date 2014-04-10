/**
 * project name: myNews
 * created at 2013-3-6 - 下午2:56:45
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuer
 * 
 */
public class Config {
	public static final String UserAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";

	public static String getProperties(String name) throws IOException {
		Properties p = new PropertiesLoader("classpath:/config.properties").getProperties();
		return p.getProperty(name);
	}

	public static String getPostUrl() throws IOException {
		return getProperties("domain") + getProperties("posturl");
	}

	public static String getLoginUrl() throws IOException {
		return getProperties("domain") + getProperties("loginurl");
	}

	public static String getModelUrl() throws IOException {
		return getProperties("domain") + getProperties("modelurl");
	}

	public static String getWeiboName() throws IOException {
		return getProperties("weiboname");
	}

	public static String getWeiboPsw() throws IOException {
		return getProperties("password");
	}

	public static String getWeiboLoginUrl() throws IOException {
		return getProperties("weibologin");
	}

	public static String getUserAgent() {
		String ua = UserAgent;
		try {
			String useragent = getProperties("useragent");
			if (StringUtils.isNotBlank(useragent)) {
				ua = useragent;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ua;
	}

	public static Boolean getIsToQSH() throws IOException {
		if (getProperties("postqsh").equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean getIsToWeibo() throws IOException {
		if (getProperties("postweibo").equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
}
