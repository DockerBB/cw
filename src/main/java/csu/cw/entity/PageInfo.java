package csu.cw.entity;

import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Component("pageInfo")
public class PageInfo {
    private int id;
    private String url;
    private String content;
    private String htmlSource;
    private String dateTime;
    private String title;
    private String country;
    private String author;
    private String city;
    private String keyworlds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String htmlSource) {
        this.htmlSource = htmlSource;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKeyworlds() {
        return keyworlds;
    }

    public void setKeyworlds(String keyworlds) {
        this.keyworlds = keyworlds;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", htmlSource='" + htmlSource + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", author='" + author + '\'' +
                ", city='" + city + '\'' +
                ", keyworlds='" + keyworlds + '\'' +
                '}';
    }
}
