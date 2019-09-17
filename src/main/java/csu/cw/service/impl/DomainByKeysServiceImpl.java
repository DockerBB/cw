package csu.cw.service.impl;

import csu.cw.dao.DomainByKeysDao;
import csu.cw.entity.DomainByKeys;
import csu.cw.service.DomainByKeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("domainByKeysService")
public class DomainByKeysServiceImpl implements DomainByKeysService{

    @Autowired
    DomainByKeysDao domainByKeysDao;

    @Override
    public boolean addDomainByKeys(DomainByKeys domainByKeys) {
        return domainByKeysDao.addDomainByKeys(domainByKeys);
    }

    @Override
    public List<DomainByKeys> findDomainByKeys(String keys) {
        return domainByKeysDao.findDomainByKeys(keys);
    }
}
