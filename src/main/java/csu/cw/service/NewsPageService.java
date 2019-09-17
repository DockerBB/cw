package csu.cw.service;

import csu.cw.entity.NewsPage;

import java.util.List;

public interface NewsPageService {

    public boolean addNewsPage(NewsPage newsPage);
    public List<NewsPage> findNewsPage(String keys);
    public List<NewsPage> findNewsByUrl(NewsPage newsPage);

}
