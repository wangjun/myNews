package com.uestc.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.uestc.news.entity.NewsFeed;
import com.uestc.news.repository.NewsFeedDao;

@Component
@Transactional(readOnly = true)
public class NewsFeedService {

	@Autowired
	private NewsFeedDao newsFeedDao;

	public List<NewsFeed> getAllNewsFeedlist() {
		return (List<NewsFeed>) newsFeedDao.findAll();
	}

	public NewsFeed findById(Long id) {
		return newsFeedDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(NewsFeed entity) {
		newsFeedDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		newsFeedDao.delete(id);
	}

}
