/**
 * project name: williamlong
 * created at 2013-3-1 - 下午2:05:08
 * author:yuer
 * email:yuerguang.cl@gmail.com
 */
package com.uestc.news.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author yuer
 * 
 */
@Entity
@Table(name = "news")
public class News extends IdEntity {
	private String title;
	private String description;
	private String author;
	private Date pubDate;
	private String link;
	private String hash;// 文章的hash
	private String catalog;// 文章的分类

	private String source;// 来源

	private Date clawTime;// 抓取时间

	private Boolean synQshp;
	private Boolean synWeibo;

	public static final String _SynQshp = "synQshp";
	public static final String _ClawTime = "clawTime";
	public static final String _SynWeibo = "synWeibo";
	public static final String _Source = "source";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(columnDefinition = "text default null comment '内容'")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "pub_date", columnDefinition = "timestamp default '0000-00-00 00:00:00' comment '发布时间'")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	@Column(name = "claw_time", columnDefinition = "timestamp default '0000-00-00 00:00:00' comment '抓取时间'")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getClawTime() {
		return clawTime;
	}

	public void setClawTime(Date clawTime) {
		this.clawTime = clawTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@JsonIgnore
	@Column(name = "syn_qshp")
	public Boolean getSynQshp() {
		return synQshp;
	}

	public void setSynQshp(Boolean synQshp) {
		this.synQshp = synQshp;
	}

	@JsonIgnore
	@Column(name = "syn_weibo")
	public Boolean getSynWeibo() {
		return synWeibo;
	}

	public void setSynWeibo(Boolean synWeibo) {
		this.synWeibo = synWeibo;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("作者：");
		sb.append(this.author);
		sb.append("分类：");
		sb.append(this.catalog);
		sb.append("正文：");
		sb.append(this.description);
		sb.append("Hash值：");
		sb.append(this.hash);
		sb.append("链接：");
		sb.append(this.link);
		sb.append("来自：");
		sb.append(this.source);
		sb.append("标题：");
		sb.append(this.title);
		return sb.toString();
	}

}
