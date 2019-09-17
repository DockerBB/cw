package csu.cw.service.impl;

import csu.cw.dao.TargetDao;
import csu.cw.entity.NewsPage;
import csu.cw.service.TargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TargetService")
public class TargetServiceImpl implements TargetService {

    @Autowired
    TargetDao targetDao;

    @Override
    public boolean addTarget(NewsPage newsPage) {
        return targetDao.addTarget(newsPage);
    }

    @Override
    public List<NewsPage> findTarget(String keys) {
        return targetDao.findTarget(keys);
    }

    @Override
    public List<NewsPage> findTargetByUrl(NewsPage newsPage) {
        return targetDao.findTargetByUrl(newsPage);
    }
}
