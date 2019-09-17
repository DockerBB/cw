package csu.cw.dao;

import csu.cw.entity.NewsPage;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@MapperScan
@Repository
public interface TargetDao {

    public boolean addTarget(NewsPage newsPage);

    public List<NewsPage> findTarget(@Param("keyworlds") String keyworlds);

    public List<NewsPage> findTargetByUrl(NewsPage newsPage);

}
