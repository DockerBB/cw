package csu.cw.test;

import csu.cw.entity.PageInfo;
import csu.cw.util.CountryAndCity;
import csu.cw.util.ExtractorTime;
import csu.cw.util.TitleAndContent;
import csu.cw.util.WriteFile;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.UUID;

public class SpiderTest implements PageProcessor{

    private PageInfo pageInfo;
    private static final String prePath = "F:\\newProjectLiuJin\\singleHtml\\";

    @Override
    public void process(Page page) {
        Selectable pageUrl = page.getUrl();
        String url = pageUrl.toString();
        List<String> countryAndCity = CountryAndCity.getCountryAndCity(url);
        String country = countryAndCity.get(0);
        String city = countryAndCity.get(1);
        Html html = page.getHtml();
        String htmlSource = html.toString();
        List<String> titleAndContent = TitleAndContent.getTitleAndContent(htmlSource);
        String title = titleAndContent.get(0);
        String content = titleAndContent.get(1);
        String time = "";
        if(title.length() > 0 && htmlSource.length() > 0){
            time = ExtractorTime.extractorTime(htmlSource, title);
        }
        UUID uuid = UUID.randomUUID();
        String fileEnd = uuid.toString().replace("-", "");
        String sourcePath = prePath + "source" + fileEnd;
        String contentPath = prePath + "content" + fileEnd;
        WriteFile.writeFile(sourcePath, htmlSource);
        WriteFile.writeFile(contentPath, content);
        pageInfo.setUrl(url);
        pageInfo.setHtmlSource(sourcePath);
        pageInfo.setContent(contentPath);
        pageInfo.setCountry(country);
        pageInfo.setCity(city);
        pageInfo.setTitle(title);
        pageInfo.setDateTime(time);
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

//    public static void main(String[] args) {
//        Spider.create(new SpiderTest())
//                .addUrl("https://www.ameribev.org/education-resources/blog/post/honoring-one-of-our-own-creating-the-next-generation-of-change-agents/")
//                .run();
//    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }
}
