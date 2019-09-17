package csu.cw.service.impl;

import csu.cw.dao.PageInfoDao;
import csu.cw.entity.NewsPage;
import csu.cw.entity.PageInfo;
import csu.cw.service.PageInfoService;
import csu.cw.util.CountryAndCity;
import csu.cw.util.ExtractorTime;
import csu.cw.util.TitleAndContent;
import csu.cw.util.WriteFile;
import csu.cw.webmagic.HttpClientSpider;
import csu.cw.webmagic.SpiderPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service("pageInfoService")
public class PageInfoServiceImpl implements PageInfoService {

    @Autowired
    PageInfoDao pageInfoDao;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public List<PageInfo> findPageInfo() {
        List<PageInfo> list = pageInfoDao.findPageInfo();
        return list;
    }

    //private static final String prePath = "F:\\newProjectLiuJin\\singleHtml\\";
    private static final String prePath = "\\src\\main\\resources\\static\\txt\\";

    @Override
    public boolean addPageInfo(PageInfo pageInfo) {
        return pageInfoDao.addPageInfo(pageInfo);
    }

    @Override
    public boolean delPageInfo(PageInfo pageInfo) {
        return pageInfoDao.delPageInfo(pageInfo);
    }

    @Override
    public boolean updatePageInfo(PageInfo pageInfo) {
        return pageInfoDao.updatePageInfo(pageInfo);
    }

    @Override
    public List<PageInfo> getPageType(String url, String key) {
        List<PageInfo> list = new ArrayList<>();
        PageInfo pageInfo = new PageInfo();
        List<String> countryAndCity = CountryAndCity.getCountryAndCity(url);
        String country = countryAndCity.get(0);
        String city = countryAndCity.get(1);
        String html = HttpClientSpider.doGet(url);
        List<String> titleAndContent = TitleAndContent.getTitleAndContent(html);
        String title = titleAndContent.get(0);
        String content = titleAndContent.get(1);
        String time = "";
//        if(title.length() > 0 && html.length() > 0){
//            time = ExtractorTime.extractorTime(html, title);
//        }
        if(title != null && html != null){
            time = ExtractorTime.extractorTime(html, title);
        }
        UUID uuid = UUID.randomUUID();
        String fileEnd = uuid.toString().replace("-", "");
        String ss = System.getProperty("user.dir") + System.getProperty("file.separator") + "file" + System.getProperty("file.separator");
//        String sourcePath = tmp + prePath + "source" + fileEnd;
//        String contentPath = tmp + prePath + "content" + fileEnd;
        //String ss = PageInfoServiceImpl.class.getClassLoader().getResource("static/html/").getPath();
        //String ss = "E:\\file\\file\\";
        String sourcePath = "source" + fileEnd;
        String contentPath = "content" + fileEnd;
        WriteFile.writeFile(ss + sourcePath, html);
        WriteFile.writeFile(ss + contentPath, content);
        pageInfo.setUrl(url);
        pageInfo.setHtmlSource(sourcePath);
        pageInfo.setContent(contentPath);
        pageInfo.setCountry(country);
        pageInfo.setCity(city);
        pageInfo.setTitle(title);
        pageInfo.setDateTime(time);
        pageInfo.setKeyworlds(key);
        pageInfoDao.getPageType(pageInfo);
        if(pageInfo.getId() > 0){
            list.add(pageInfo);
        }
        return list;
    }

    @Override
    public List<PageInfo> selectPageInfoByUrl(String url, String key, List<NewsPage> listNews) {
        List<PageInfo> list;
        if("".equals(key)){
            list = pageInfoDao.selectPageInfoByUrl(url);
            if(list.size() <= 0){
                getPageType(url, key);
                list = pageInfoDao.selectPageInfoByUrl(url);
            }
        }else{
            if(listNews != null){
                for(int i=0; i<listNews.size(); i++){
                    PageInfo pageInfo = new PageInfo();
                    List<String> countryAndCity = CountryAndCity.getCountryAndCity(listNews.get(i).getNewsUrl());
                    String country = countryAndCity.get(0);
                    String city = countryAndCity.get(1);
                    pageInfo.setCountry(country);
                    pageInfo.setCity(city);
                    pageInfo.setUrl(listNews.get(i).getNewsUrl());
                    pageInfo.setKeyworlds(listNews.get(i).getKeyworlds());
                    threadPoolExecutor.execute(new SpiderPageInfo(pageInfo, pageInfoDao));
                }
                threadPoolExecutor.shutdown();
                while (true){
                    if(threadPoolExecutor.isTerminated()){
                        System.out.println("执行完毕");
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            list = pageInfoDao.selectPageInfoByKey(key);
        }


        return list;
        //return pageInfoDao.selectPageInfoByUrl(url);
    }

    @Override
    public List<PageInfo> selectPageInfoByKey(String key) {
        List<PageInfo> list = pageInfoDao.selectPageInfoByKey(key);
        return list;
    }
}
