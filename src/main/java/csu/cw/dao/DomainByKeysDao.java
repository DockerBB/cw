package csu.cw.dao;

import csu.cw.entity.DomainByKeys;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@MapperScan
@Repository
public interface DomainByKeysDao {

    public boolean addDomainByKeys(DomainByKeys domainByKeys);

    public List<DomainByKeys> findDomainByKeys(@Param("keyworlds") String keyworlds);

}
