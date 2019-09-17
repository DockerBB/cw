package csu.cw.util;

import csu.cw.entity.DomainByKeys;
import csu.cw.service.DomainByKeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Set;

@Component
public class SpringPipeLine implements Pipeline {

    @Autowired
    DomainByKeysService domainByKeysService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Set<String> urlSet = resultItems.get("urlSet");
        String keys = resultItems.get("keys");
        Object lock = resultItems.get("lock");
        if(urlSet != null && keys != null){
            for (String str : urlSet){
                DomainByKeys domainByKeys = new DomainByKeys();
                domainByKeys.setDoMainName(str);
                domainByKeys.setKeyworlds(keys);
                synchronized (lock){
                    domainByKeysService.addDomainByKeys(domainByKeys);
                }
            }
        }

    }
}
