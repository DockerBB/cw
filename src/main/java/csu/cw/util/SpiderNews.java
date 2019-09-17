package csu.cw.util;

import csu.cw.entity.NewsPage;
import csu.cw.service.NewsPageService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class SpiderNews implements PageProcessor,Runnable {

    private String url;
    private NewsPageService newsPageService;
    private String keyWorldList;

    public SpiderNews(String url, NewsPageService newsPageService, String keyWorldList){
        this.url = url;
        this.newsPageService = newsPageService;
        this.keyWorldList = keyWorldList;
    }

    public SpiderNews(NewsPageService newsPageService, String keyWorldList){
        this.newsPageService = newsPageService;
        this.keyWorldList = keyWorldList;
    }

    @Transactional
    @Override
    public void process(Page page) {
        String pageUrl = page.getUrl().toString();
        String html = page.getHtml().toString();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a");
        for(Element element : elements){
            String aTest = element.text();
            if(aTest == null || aTest.equals("")){
                continue;
            }else if(aTest.contains("news") || aTest.contains("News") || aTest.contains("NEWS") || aTest.contains("events") || aTest.contains("Events") || aTest.contains("EVENTS") || aTest.contains("blog") || aTest.contains("Blog") || aTest.contains("BLOG") || aTest.contains("Media") || aTest.contains("media")){
                String hrefString = element.attr("href");
                if(!hrefString.contains("http")){
                    String string = "";
                    if(pageUrl != null){
                        string = getHost(pageUrl);
                    }else{
                        string = getHost(url);
                    }

                    hrefString = string + hrefString;
                }
                NewsPage newsPage = new NewsPage();
                newsPage.setNewsUrl(hrefString);
                newsPage.setKeyworlds(keyWorldList);
                synchronized (newsPageService){
                    if(newsPageService.findNewsByUrl(newsPage).size() <= 0){
                        newsPageService.addNewsPage(newsPage);
                    }
                }

            }
        }
    }

    private Site site = Site.me()
            .setTimeOut(40*1000)
            .setRetrySleepTime(10000)
            .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("accept-encoding", "gzip, deflate")
            .addHeader("accept-language", "zh-CN,zh;q=0.9")
            .setUserAgent("user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void run() {
        Spider.create(new SpiderNews(url, newsPageService, keyWorldList))
                .addUrl(url)
                .run();
    }

    public String getHost(String pageUrl){
        String httpsString = pageUrl.substring(0, url.indexOf("/") + 2);
        String endString = pageUrl.substring(url.indexOf("//") + 2);
        String startString = "";
        if(endString.indexOf("/") > 0){
            String hostString = endString.substring(0, endString.indexOf("/"));
            startString = httpsString + hostString;
        }else{
            startString = url;
        }
        return startString;
    }
}
