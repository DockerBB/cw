package csu.cw.util;

import csu.cw.entity.NewsPage;
import csu.cw.service.NewsPageService;
import csu.cw.service.TargetService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class SpiderTarget implements PageProcessor,Runnable {

    private String url;
    private TargetService targetService;
    private String keyWorldList;

    public SpiderTarget(String url, TargetService targetService, String keyWorldList){
        this.url = url;
        this.targetService = targetService;
        this.keyWorldList = keyWorldList;
    }

    public SpiderTarget(TargetService targetService, String keyWorldList){
        this.targetService = targetService;
        this.keyWorldList = keyWorldList;
    }

    @Transactional
    @Override
    public void process(Page page) {
        String pageUrl = page.getUrl().toString();
        String html = page.getHtml().toString();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div");
        for(int i=elements.size()-1; i>=0; i--){
            String tmpId = elements.get(i).id();
            String tmpClass = elements.get(i).classNames().toString();
            boolean flag = false;
            if(tmpId != null && tmpId.contains("content")){
                //Elements elements1 = elements.get(i).children();
                String query = "div#" + tmpId + " a";
                Elements elements1 = document.select(query);
                for(Element element : elements1){
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
                    flag = true;
                    synchronized (targetService){
                        if(targetService.findTargetByUrl(newsPage).size() <= 0){
                            targetService.addTarget(newsPage);
                        }
                    }
                }
                if(flag){
                    break;
                }
            }
            if(flag && tmpClass != null && tmpClass.contains("content")){
                String query = "div." + tmpClass + " a";
                Elements elements1 = document.select(query);
                for(Element element : elements1){
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
                    synchronized (targetService){
                        if(targetService.findTargetByUrl(newsPage).size() <= 0){
                            targetService.addTarget(newsPage);
                        }
                    }
                }
                break;
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
        Spider.create(new SpiderTarget(url, targetService, keyWorldList))
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
