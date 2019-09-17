package csu.cw.webmagic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class Selenium {

    public Set<String> doGet(String url){
        WebDriver driver = getWebDriver();
        //String string = "air purification system";
//        String keyWorld = string.replaceAll(" ", "+");
//        if(keyWorld.lastIndexOf("+") == (keyWorld.length()-1)){
//            keyWorld.substring(0, keyWorld.length()-1);
//        }
        driver.get(url);//"https://cn.bing.com/search?q=" + keyWorld
        boolean count = true;
        int beforeFlag = -1;
        Set<String> urlSet = new HashSet<String>();
        while(urlSet.size() < 200){
            System.out.println("爬取了" + urlSet.size() + "个网页");
            if(urlSet.size() == beforeFlag){
                break;
            }
            beforeFlag = urlSet.size();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Document document = Jsoup.parse(driver.getPageSource());
            if(count){
                driver.findElements(By.id("est_en")).get(0).click();
                count = false;
            }
            Elements elements = document.select("ol#b_results li.b_algo h2 a");
            for (Element element : elements){
                String tmp = element.attr("href");
                String startString = tmp.substring(0, tmp.indexOf("//") + 2);
                String tmpString = tmp.substring(tmp.indexOf("//") + 2);
                String domain = tmpString.substring(0, tmpString.indexOf("/"));
                String result = startString + domain;
                if(urlSet.size() <= 200){
                    urlSet.add(result);
                }
            }
            Elements elements1 = document.select("ol#b_results li.b_pag ul li a.sb_pagN");
            if(elements1.size() > 0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driver.findElements(By.cssSelector("ol#b_results li.b_pag ul li a.sb_pagN")).get(0).click();
            }else{
                break;
            }
        }
        driver.close();
        return urlSet;
    }


    /**
     * 路径改动1
     * **/
    public WebDriver getWebDriver(){
        String ss = System.getProperty("user.dir") + System.getProperty("file.separator") + "chromedriver.exe";
//        String tmp1 = tmp + "\\cw\\src\\main\\resources\\chromedriver.exe";
        //String ss = Selenium.class.getClassLoader().getResource("chromedriver.exe").getPath();
        //String ss= "E:\\file\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", ss);
        ChromeOptions chromeOptions = new ChromeOptions();
        List<String> headerList = new ArrayList<String>();
        headerList.add("accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headerList.add("accept-encoding=gzip, deflate");
        headerList.add("accept-language=zh-CN,zh;q=0.9");
        headerList.add("user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");
        chromeOptions.addArguments(headerList);
        chromeOptions.addArguments("headless");
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
        return driver;
    }
}
