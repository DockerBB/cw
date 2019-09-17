package csu.cw.service;

import csu.cw.entity.NewsPage;
import csu.cw.entity.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PageInfoService {
    public List<PageInfo> findPageInfo();

    public boolean addPageInfo(PageInfo pageInfo);

    public boolean delPageInfo(PageInfo pageInfo);

    public boolean updatePageInfo(PageInfo pageInfo);

    public List<PageInfo> getPageType(String url, String key);

    public List<PageInfo> selectPageInfoByUrl(String url, String key, List<NewsPage> listNews);

    public List<PageInfo> selectPageInfoByKey(String key);

}
