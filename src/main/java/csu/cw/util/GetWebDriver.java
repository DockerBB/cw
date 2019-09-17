package csu.cw.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetWebDriver {

    public WebDriver getWebDriver(){
        String ss= "E:\\file\\chromedriver.exe";
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
        driver.manage().timeouts().pageLoadTimeout(20000, TimeUnit.MILLISECONDS);
        return driver;
    }

}
