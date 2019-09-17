package csu.cw.service.impl;

import csu.cw.dao.NewsPageDao;
import csu.cw.entity.NewsPage;
import csu.cw.service.NewsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("newsPageService")
public class NewsPageServiceImpl implements NewsPageService {

    @Autowired
    NewsPageDao newsPageDao;

    @Override
    public boolean addNewsPage(NewsPage newsPage) {
        return newsPageDao.addNewsPage(newsPage);
    }

    @Override
    public List<NewsPage> findNewsPage(String keys) {
        return newsPageDao.findNewsPage(keys);
    }

    @Override
    public List<NewsPage> findNewsByUrl(NewsPage newsPage) {
        return newsPageDao.findNewsByUrl(newsPage);
    }
}
