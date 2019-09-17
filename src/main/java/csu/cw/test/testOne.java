package csu.cw.test;

import csu.cw.dao.PageInfoDao;
import csu.cw.entity.PageInfo;
import csu.cw.service.PageInfoService;
import csu.cw.service.TargetService;
import csu.cw.service.impl.PageInfoServiceImpl;
import csu.cw.service.impl.TargetServiceImpl;
import csu.cw.util.SpiderTarget;
import csu.cw.webmagic.BingWebmagicSpider;
import csu.cw.webmagic.HttpClientSpider;
import csu.cw.webmagic.Selenium;
import csu.cw.webmagic.UrlSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import us.codecraft.webmagic.Spider;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class testOne {

    @Autowired
    private PageInfoService pageInfoService;

    @Autowired
    private TargetService targetService;

    public PageInfoService getPageInfoService() {
        return pageInfoService;
    }

//    public static void main(String[] args) {
////        UrlSpider urlSpider = new UrlSpider();
////        Spider.create(urlSpider)
////                .addUrl("https://www.ameribev.org/education-resources/blog/post/honoring-one-of-our-own-creating-the-next-generation-of-change-agents/")
////                .run();
////        PageInfo pageInfo = urlSpider.getPageInfo();
////        System.out.println(pageInfo.getUrl());
//        //HttpClientSpider.doGet("https://www.ameribev.org/education-resources/blog/post/honoring-one-of-our-own-creating-the-next-generation-of-change-agents/");
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15, 3, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<Runnable>(20000), new ThreadPoolExecutor.DiscardOldestPolicy());
//        Object lock = new Object();
//        String keys = "advanced bottling line";
//        threadPoolExecutor.execute(new BingWebmagicSpider(keys, lock));
//        //threadPoolExecutor.shutdown();
//    }

    public static void main(String[] args) {
//        Selenium selenium = new Selenium();
//        Set<String> strings = selenium.doGet();
//        for(String str : strings){
//            System.out.println(str);
//        }
        //String ss = testOne.class.getClassLoader().getResource("chromedriver.exe").getPath();
//        String ss = testOne.class.getClassLoader().getResource("static/textFile").getPath();
//        String tmp = System.getProperty("user.dir");
//        String tmp1 = tmp + "\\cw\\src\\main\\resources\\chromedriver.exe";
        Thread thread = new Thread(new SpiderTarget("https://www.brewers.org.au/media.html", new TargetServiceImpl(), "test"));
        thread.start();
    }




}
