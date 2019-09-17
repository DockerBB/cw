package csu.cw.webmagic;

import csu.cw.dao.PageInfoDao;
import csu.cw.entity.PageInfo;
import csu.cw.util.ExtractorTime;
import csu.cw.util.TitleAndContent;
import csu.cw.util.WriteFile;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.UUID;

public class SpiderPageInfo implements PageProcessor, Runnable {

    private final static String ss = "E:\\file\\file\\";

    private PageInfo pageInfo;

    private PageInfoDao pageInfoDao;

    public SpiderPageInfo(PageInfo pageInfo, PageInfoDao pageInfoDao){
        this.pageInfo = pageInfo;
        this.pageInfoDao = pageInfoDao;
    }

    @Override
    public void run() {
        Spider.create(new SpiderPageInfo(pageInfo, pageInfoDao))
                .addUrl(pageInfo.getUrl())
                .run();
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
    public void process(Page page) {
        String html = page.getHtml().toString();
        UUID uuid = UUID.randomUUID();
        String fileEnd = uuid.toString().replace("-", "");
        String sourcePath = "source" + fileEnd;
        String contentPath = "content" + fileEnd;
        List<String> titleAndContent = TitleAndContent.getTitleAndContent(html);
        String title = titleAndContent.get(0);
        String content = titleAndContent.get(1);
        String time = "";
        if(title != null && html != null){
            time = ExtractorTime.extractorTime(html, title);
        }
        if(content.length() > 10){
            WriteFile.writeFile(ss + sourcePath, html);
            WriteFile.writeFile(ss + contentPath,content);
            pageInfo.setTitle(title);
            pageInfo.setHtmlSource(sourcePath);
            pageInfo.setContent(contentPath);
            pageInfo.setDateTime(time);
            synchronized (pageInfoDao){
                pageInfoDao.getPageType(pageInfo);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
