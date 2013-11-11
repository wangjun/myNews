package com.uestc.news.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.uestc.news.entity.Channel;
import com.uestc.news.entity.News;
import com.uestc.news.repository.NewsDao;
import com.uestc.news.utils.DynamicSpecifications;
import com.uestc.news.utils.SearchFilter;
import com.uestc.news.utils.SearchFilter.Operator;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class NewsService {
	private static Logger logger = LoggerFactory.getLogger(NewsService.class);
	@Autowired
	private NewsDao newsDao;

	@Autowired
	private WeiboService weiboService;

	public News getNews(Long id) {
		return newsDao.findOne(id);
	}

	public News findByHash(String hash) {
		return newsDao.findByHash(hash);
	}

	@Transactional(readOnly = false)
	public void saveNews(News entity) {
		newsDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteNews(Long id) {
		newsDao.delete(id);
	}

	public List<News> getAllNews() {
		return (List<News>) newsDao.findAll();
	}

	/**
	 * @param channel
	 */
	@Transactional(readOnly = false)
	public String saveNewsByChannel(Channel channel) {
		if (channel == null || channel.getItem().size() < 1) {
			return "fail";
		}
		for (News n : channel.getItem()) {
			News tempNews = findByHash(n.getHash());
			if (tempNews == null) {
				this.saveNews(n);
				logger.info("新添加一条新闻，标题：" + n.getTitle() + "链接：" + n.getLink());
			} else {
				continue;
			}
		}

		return "ok";
	}

	/**
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param sortTypes
	 * @return
	 */
	public Page<News> getNews(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType, String order, Map<String, String> sortTypes) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType, order, sortTypes);
		Specification<News> spec = buildSpecification(searchParams);
		return newsDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 * 
	 * @param sortTypes
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType, String order, Map<String, String> sortTypes) {
		Sort sort = null;
		if (!sortType.equalsIgnoreCase("auto") && sortTypes.keySet().contains(sortType)) {
			if ("asc".equalsIgnoreCase(order)) {
				sort = new Sort(Direction.ASC, sortType);
			} else {
				sort = new Sort(Direction.DESC, sortType);
			}
		} else {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<News> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<News> spec = DynamicSpecifications.bySearchFilter(filters.values(), News.class);
		return spec;
	}

	public List<News> findAllByTime() {
		Sort sort = new Sort(Direction.DESC, "id");
		SearchFilter filter = new SearchFilter(News._SynQshp, Operator.EQ, false);
		SearchFilter filter2 = new SearchFilter(News._Source, Operator.EQ, "知乎");
		List<News> news = newsDao.findAll(DynamicSpecifications.bySearchFilter(Lists.newArrayList(filter,filter2), News.class), sort);
		return news;
	}

	/**
	 * 返回最新没有同步到weibo上的新闻
	 * @return
	 */
	public News getNewsMaxId() {
		Sort sort = new Sort(Direction.DESC, "id");
		SearchFilter filter = new SearchFilter(News._SynWeibo, Operator.EQ, false);
		List<News> news = newsDao.findAll(DynamicSpecifications.bySearchFilter(Lists.newArrayList(filter), News.class), sort);
		if (news == null || news.size() == 0) {
			return null;
		} else {
			return news.get(0);
		}
	}
}
