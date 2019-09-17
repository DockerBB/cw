package csu.cw.dao;

import csu.cw.entity.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@MapperScan
@Repository
public interface PageInfoDao {

    public List<PageInfo> findPageInfo();

    public boolean addPageInfo(PageInfo pageInfo);

    public boolean delPageInfo(PageInfo pageInfo);

    public boolean updatePageInfo(PageInfo pageInfo);

    public boolean getPageType(PageInfo pageInfo);

    public List<PageInfo> selectPageInfoByUrl(@Param("url") String url);

    public List<PageInfo> selectPageInfoByKey(@Param("key") String key);

}
