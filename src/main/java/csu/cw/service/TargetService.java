package csu.cw.service;

import csu.cw.entity.NewsPage;

import java.util.List;

public interface TargetService {

    public boolean addTarget(NewsPage newsPage);
    public List<NewsPage> findTarget(String keys);
    public List<NewsPage> findTargetByUrl(NewsPage newsPage);

}
