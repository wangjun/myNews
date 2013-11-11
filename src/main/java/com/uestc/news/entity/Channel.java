/**
 * project name: williamlong
 * created at 2013-3-1 - 下午1:58:18
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.entity;

import java.util.List;

import javax.persistence.MappedSuperclass;

/**
 * @author yuer
 * 
 */
@MappedSuperclass
public class Channel {
	private String title;
	private String link;
	private String description;
	private String language;
	private List<News> item;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<News> getItem() {
		return item;
	}

	public void setItem(List<News> item) {
		this.item = item;
	}
}
