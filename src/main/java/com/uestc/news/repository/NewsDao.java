package com.uestc.news.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.uestc.news.entity.News;

public interface NewsDao extends PagingAndSortingRepository<News, Long>, JpaSpecificationExecutor<News> {
	News findByHash(String hash);
}
