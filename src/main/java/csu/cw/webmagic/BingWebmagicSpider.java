package csu.cw.webmagic;

import csu.cw.entity.DomainByKeys;
import csu.cw.util.SpringPipeLine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashSet;
import java.util.Set;

public class BingWebmagicSpider implements PageProcessor, Runnable{

//    @Autowired
//    private SpringPipeLine springPipeLine;

    public Set<String> urlSet;
    public String keyWorlds;
    public static String startString = "https://cn.bing.com/search?q=";
    public Object lock;

    public BingWebmagicSpider(String keyWorlds, Object lock){
        this.keyWorlds = keyWorlds;
        this.lock = lock;
        this.urlSet = new HashSet<>();
    }


    @Override
    public void run() {
        System.out.println("开始爬取" + keyWorlds + "这个关键字的源url");
        String urlString = startString + standardUrl(keyWorlds);
        Spider.create(new BingWebmagicSpider(keyWorlds, lock))
                .addUrl(urlString)
                //.addPipeline(this.springPipeLine)
                .run();
    }

    @Override
    public void process(Page page) {
        Selectable pageUrl = page.getUrl();
        String string = pageUrl.toString();
        String httpsString = string.substring(0, string.indexOf("//")+2);
        String endString = string.substring(string.indexOf("//") + 2);
        String hostString = endString.substring(0, endString.indexOf("/"));
        String startString = httpsString + hostString;
        if(urlSet.size() < 200){
            Html html = page.getHtml();
            Elements elements = Jsoup.parse(html.toString()).select("ol#b_results li.b_algo h2 a");
            if(elements.size() > 0){
                for (Element element : elements){
                    String tmp = element.attr("href");
                    System.out.println(tmp);
                    page.addTargetRequest(tmp);
                }
                Elements elements1 = Jsoup.parse(html.toString()).select("ol#b_results li.b_pag ul li a.sb_pagN");
                if(elements1.size() > 0){
                    String href = elements1.get(0).attr("href");
                    String result = "https://cn.bing.com" + href;
                    page.addTargetRequest(result);
                }
            }else{
                parseHtml(html, startString);
            }
        }else{
//            for(String str : urlSet){
//                DomainByKeys domainByKeys = new DomainByKeys();
//                domainByKeys.setDoMain(str);
//                domainByKeys.setKeys(keyWorlds);
//
//                //MybatisTestBing.insertTkey(tkey);
//            }
            page.putField("urlSet", urlSet);
            page.putField("keys", keyWorlds);
            page.putField("lock", lock);
            return;
        }
    }

    private Site site = Site.me()
            .setTimeOut(10*1000)
            .setRetrySleepTime(5000)
            .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("accept-encoding", "gzip, deflate")
            .addHeader("accept-language", "zh-CN,zh;q=0.9")
            .setUserAgent("user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return site;
    }

    public String standardUrl(String key){
        String keys = key.replaceAll(" ", "+");
        if(keys.lastIndexOf("+") == keys.length() - 1){
            keys.substring(0, keyWorlds.length() - 1);
        }
        return keys;
    }

    public void parseHtml(Html html, String preString){
        Elements elements = Jsoup.parse(html.toString()).select("a");
        for(Element element : elements){
            String aTest = element.text();
            if (aTest == null || aTest.equals("")) {
                continue;
            }else if(aTest.contains("news") || aTest.contains("News") || aTest.contains("NEWS") || aTest.contains("events") || aTest.contains("Events") || aTest.contains("EVENTS") || aTest.contains("blog") || aTest.contains("Blog") || aTest.contains("BLOG") ){
                String hrefString = element.attr("href");
                if(!hrefString.contains("http")){
                    hrefString = preString + hrefString;
                }
                urlSet.add(hrefString);
            }
        }
    }
}
