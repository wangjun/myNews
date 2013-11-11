package com.uestc.news.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;

public class JPDateUtils extends DateUtils {

	public static long RequestDelayTime = 10 * MILLIS_PER_MINUTE;

	/**
	 * date to XMLGregorianCalendar
	 * 
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar toXMLCalendar(Date date) {
		DatatypeFactory df = null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			System.out.println(e.getMessage());
		}

		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(date);

		return df.newXMLGregorianCalendar(cal1);
	}

	public static Date fromXMLCalendar(XMLGregorianCalendar c) {
		if (c == null)
			return null;

		return c.toGregorianCalendar().getTime();
	}

	public static String toString(Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (sdf.format(date));
		}
	}

	public static String toCNString(Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.format(date);
		}
	}

	public static String toString(Date date, String format) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return (sdf.format(date));
		}
	}

	// index: 时间(Date)的 index -1：昨天， 0-今天，1：明天
	public static Date getDay(Date date, int index) {
		date = DateUtils.setHours(date, 0);
		date = DateUtils.setMinutes(date, 0);
		date = DateUtils.setSeconds(date, 0);
		date = DateUtils.setMilliseconds(date, 0);
		return DateUtils.addDays(date, index);
	}

	// index: 0：今天；1：明天； -1：昨天
	public static Date getDay(int index) {
		Date date = new Date();
		date = DateUtils.setHours(date, 0);
		date = DateUtils.setMinutes(date, 0);
		date = DateUtils.setSeconds(date, 0);
		date = DateUtils.setMilliseconds(date, 0);
		return DateUtils.addDays(date, index);
	}

	/**
	 * 获取当月第一天
	 * 
	 * @return
	 */
	public static Date getCurrentMonth() {
		Date date = new Date();
		date = DateUtils.setDays(date, 1);
		date = DateUtils.setHours(date, 0);
		date = DateUtils.setMinutes(date, 0);
		date = DateUtils.setSeconds(date, 0);
		date = DateUtils.setMilliseconds(date, 0);
		return date;
	}

	/**
	 * 将中文格式的日期2013年04月09日17:35装换为Date类型
	 * 
	 * @param cnDate
	 * @return
	 * @throws ParseException
	 */
	public static Date toEng(String cnDate) {
		String[] parsers = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm", "yyyy-MM-dd", "yyyy年MM月dd日hh:mm" };
		Date date = new Date();
		try {
			date = DateUtils.parseDate(cnDate, parsers);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
