package com.uestc.news.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.uestc.news.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	User findByLoginName(String loginName);
}
