package csu.cw.dao;

import csu.cw.entity.DomainByKeys;
import csu.cw.entity.NewsPage;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@MapperScan
@Repository
public interface NewsPageDao {

    public boolean addNewsPage(NewsPage newsPage);
    public List<NewsPage> findNewsPage(@Param("keyworlds") String keyworlds);
    public List<NewsPage> findNewsByUrl(NewsPage newsPage);

}
