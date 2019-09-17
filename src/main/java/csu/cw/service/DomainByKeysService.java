package csu.cw.service;

import csu.cw.entity.DomainByKeys;

import java.util.List;

public interface DomainByKeysService {

    public boolean addDomainByKeys(DomainByKeys domainByKeys);

    public List<DomainByKeys> findDomainByKeys(String keys);

}
