package csu.cw.entity;

import org.springframework.stereotype.Component;

@Component("domainByKeys")
public class DomainByKeys {

    private int id;
    private String doMainName;
    private String keyworlds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoMainName() {
        return doMainName;
    }

    public void setDoMainName(String doMainName) {
        this.doMainName = doMainName;
    }

    public String getKeyworlds() {
        return keyworlds;
    }

    public void setKeyworlds(String keyworlds) {
        this.keyworlds = keyworlds;
    }
}
