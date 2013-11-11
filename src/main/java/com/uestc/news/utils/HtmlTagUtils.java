/**
 * project name: myNews
 * created at 2013-3-7 - 下午1:30:09
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.utils;

import java.util.regex.Pattern;

/**
 * @author yuer
 * 
 */
public class HtmlTagUtils {
	public static String getonerow(String HTMLStr) {
		String htmlStr = HTMLStr.replaceAll("<br>", "\n");
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>";
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>";
			String regEx_html = "<[^>]+>";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll("");
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll("");
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll("");
			textStr = htmlStr.replaceAll("&nbsp;", " ");
			textStr = htmlStr.replaceAll("<", "<");
			textStr = htmlStr.replaceAll(">", ">");
			textStr = htmlStr.replaceAll("®", "®");
			textStr = htmlStr.replaceAll("&", "&");
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;
	}
}
