package com.uestc.news.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "news_feed")
public class NewsFeed extends IdEntity {
	private String feedUrl;
	private String feedDesc;
	private String feedLogo;
	private int status;
	private Date createTime;

	@Column(name = "feed_url")
	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	@Column(name = "feed_desc")
	public String getFeedDesc() {
		return feedDesc;
	}

	public void setFeedDesc(String feedDesc) {
		this.feedDesc = feedDesc;
	}

	@Column(name = "feed_logo")
	public String getFeedLogo() {
		return feedLogo;
	}

	public void setFeedLogo(String feedLogo) {
		this.feedLogo = feedLogo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "create_time", columnDefinition = "timestamp default '0000-00-00 00:00:00' comment '创建时间'")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "NewsFeed [feedUrl=" + feedUrl + ", feedDesc=" + feedDesc + ", feedLogo=" + feedLogo + ", status=" + status + ", createTime=" + createTime + "]";
	}

}
