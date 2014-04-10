package com.uestc.news.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.uestc.news.entity.NewsFeed;

public interface NewsFeedDao extends PagingAndSortingRepository<NewsFeed, Long>, JpaSpecificationExecutor<NewsFeed> {
}
