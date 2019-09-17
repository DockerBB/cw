package csu.cw.controller;

import com.github.pagehelper.PageHelper;
import csu.cw.entity.DomainByKeys;
import csu.cw.entity.NewsPage;
import csu.cw.entity.PageInfo;
import csu.cw.service.DomainByKeysService;
import csu.cw.service.NewsPageService;
import csu.cw.service.PageInfoService;
import csu.cw.service.TargetService;
import csu.cw.util.ReadFile;
import csu.cw.util.SpiderNews;
import csu.cw.util.SpiderTarget;
import csu.cw.webmagic.Selenium;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;


/**
 * 文件路径改动 PageInfoServiceImpl ss
 * CountryAndCity tmp1
 * PageInfoController ss
 * **/
@RestController
@RequestMapping("/pageInfo")
public class PageInfoController {

    private final static String ss = System.getProperty("user.dir") + System.getProperty("file.separator") + "file" + System.getProperty("file.separator");
    //private final static String ss = "E:\\file\\file\\";

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20000),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    @Autowired
    PageInfoService pageInfoService;

    @Autowired
    DomainByKeysService domainByKeysService;

    @Autowired
    NewsPageService newsPageService;

    @Autowired
    TargetService targetService;

    private static final String searchPre = "https://cn.bing.com/search?q=";

    @RequestMapping(value="findPageInfo")
    public List<PageInfo> findPageInfo(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "5") int pageSize, @RequestParam(value = "url") String url){
        //System.out.println(pageInfoService.findPageInfo());
        List<PageInfo> list = new ArrayList<>();
//        if(url == null || url.length() == 0){
//            return list;
//        }
//        PageHelper.startPage(pageNum, pageSize);
//        list = pageInfoService.findPageInfo();
        return list;
    }
    @RequestMapping(value="getPageType")
    @ResponseBody
    public List<PageInfo> getPageType(String url){
        return pageInfoService.getPageType(url, "");
    }

    @RequestMapping(value="selectPageInfo")
    @ResponseBody
    public JSONObject selectPageInfo(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "5") int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<PageInfo> list = pageInfoService.findPageInfo();
        for(int i=0; i< list.size(); i++){
            PageInfo pageInfo = list.get(i);
            pageInfo.setHtmlSource(ReadFile.readFile(ss + pageInfo.getHtmlSource()).replaceAll(">", "~"));
            pageInfo.setContent(ReadFile.readFile(ss + pageInfo.getContent()));
        }
        com.github.pagehelper.PageInfo pageInfo = new com.github.pagehelper.PageInfo(list, 5);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageInfo", pageInfo);
        return jsonObject;
    }

    @RequestMapping(value="selectPageInfoByUrl")
    @ResponseBody
    public JSONObject selectPageInfoByUrl(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "5") int pageSize, @RequestParam(value = "url") String url){
        System.out.println("接受到请求");
        JSONObject jsonObject = new JSONObject();
        com.github.pagehelper.PageInfo pageInfoResult = null;
        boolean flag = false;
        if(url.contains("http")){
            PageHelper.startPage(pageNum, pageSize);
            List<PageInfo> list = pageInfoService.selectPageInfoByUrl(url, "", null);
            for(int i=0; i< list.size(); i++){
                PageInfo pageInfo = list.get(i);
                pageInfo.setHtmlSource(ReadFile.readFile(ss + pageInfo.getHtmlSource()).replaceAll(">", "~"));
                pageInfo.setContent(ReadFile.readFile(ss + pageInfo.getContent()));
            }
            pageInfoResult = new com.github.pagehelper.PageInfo(list, 5);//每次连续显示多少页
            jsonObject.put("pageInfo", pageInfoResult);
            System.out.println("返回成功");
        }else{
            List<PageInfo> list = pageInfoService.selectPageInfoByKey(url);
            List<PageInfo> results = new ArrayList<>();
            if(list.size() <= 0){
                List<NewsPage> listNews = targetService.findTarget(url);
                if(listNews.size() <= 0){
                    flag = true;
                }else{
//                    for(int i=0; i<listNews.size(); i++){
//                        List<PageInfo> pageInfos = pageInfoService.selectPageInfoByUrl("", url, listNews);
//                        if(pageInfos.size() > 0){
//                            PageInfo pageInfo = pageInfos.get(0);
//                            pageInfo.setHtmlSource(ReadFile.readFile(ss + pageInfo.getHtmlSource()).replaceAll(">", "~"));
//                            pageInfo.setContent(ReadFile.readFile(ss + pageInfo.getContent()));
//                            results.add(pageInfo);
//                        }
//                    }
                    List<PageInfo> pageInfos = pageInfoService.selectPageInfoByUrl("", url, listNews);
                    PageHelper.startPage(pageNum, pageSize);
                    results = pageInfoService.selectPageInfoByKey(url);
                    for(int i=0; i<results.size(); i++){
                        PageInfo tmp = results.get(i);
                        tmp.setHtmlSource(ReadFile.readFile(ss + tmp.getHtmlSource()).replaceAll(">", "~"));
                        tmp.setContent(ReadFile.readFile(ss + tmp.getContent()));
                    }
                    pageInfoResult = new com.github.pagehelper.PageInfo(results, 5);
                    jsonObject.put("pageInfo", pageInfoResult);
                }
            }
        }
        if(flag){
            jsonObject.put("message", "success");
        }else{
            jsonObject.put("message", "false");
        }
        System.out.println("处理完成");
        return jsonObject;
    }

    @RequestMapping(value="selectPageInfoBykeys")
    @ResponseBody
    public JSONObject selectPageInfoBykeys(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "keys") String keys){
        String keyWorld = keys.replaceAll(" ", "+");
        List<DomainByKeys> list;
        if(keyWorld.lastIndexOf("+") == (keyWorld.length()-1)){
            keyWorld.substring(0, keyWorld.length()-1);
        }
        PageHelper.startPage(pageNum, pageSize);
        list = domainByKeysService.findDomainByKeys(keys);
        if(list.size() <=0){
            System.out.println("数据中没有，去爬去");
            String url = searchPre + keyWorld;
            Selenium selenium = new Selenium();
            Set<String> strings = selenium.doGet(url);
            if(strings.size() > 0){
                for(String string : strings){
                    DomainByKeys domainByKeys = new DomainByKeys();
                    domainByKeys.setKeyworlds(keys);
                    domainByKeys.setDoMainName(string);
                    domainByKeysService.addDomainByKeys(domainByKeys);
                }
                System.out.println("爬取完毕，插入到数据库中");
                PageHelper.startPage(pageNum, pageSize);
                list = domainByKeysService.findDomainByKeys(keys);
            }else{
                list = new ArrayList<>();
            }
        }
        com.github.pagehelper.PageInfo pageInfo = new com.github.pagehelper.PageInfo(list, 5);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageInfo", pageInfo);
        return jsonObject;
    }


    @RequestMapping(value="addPageInfo")
    public boolean addPageInfo(PageInfo pageInfo){
        return pageInfoService.addPageInfo(pageInfo);
    }

    @RequestMapping(value="selectPageInfoGetNews")
    @ResponseBody
    public JSONObject selectNewsByKeys(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "keys") String keys){
        String keyWorld = keys.replaceAll(" ", "+");
        List<DomainByKeys> list;
        List<NewsPage> resultList;
        if(keyWorld.lastIndexOf("+") == (keyWorld.length()-1)){
            keyWorld.substring(0, keyWorld.length()-1);
        }
        //PageHelper.startPage(pageNum, pageSize);
        list = domainByKeysService.findDomainByKeys(keys);
        if(list.size() <=0){
            System.out.println("数据中没有主域名，去爬去");
            String url = searchPre + keyWorld;
            Selenium selenium = new Selenium();
            Set<String> strings = selenium.doGet(url);
            if(strings.size() > 0){
                for(String string : strings){
                    DomainByKeys domainByKeys = new DomainByKeys();
                    domainByKeys.setKeyworlds(keys);
                    domainByKeys.setDoMainName(string);
                    domainByKeysService.addDomainByKeys(domainByKeys);
                }
                System.out.println("爬取主域名完毕，插入到数据库完成");
            }
            list = domainByKeysService.findDomainByKeys(keys);
        }
        PageHelper.startPage(pageNum, pageSize);
        resultList = newsPageService.findNewsPage(keys);
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 3,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20000),
//                new ThreadPoolExecutor.DiscardOldestPolicy());
        if(resultList.size() <= 0){
            System.out.println("开始爬取新闻类url");
            for(DomainByKeys domainByKeys : list){
                threadPoolExecutor.execute(new SpiderNews(domainByKeys.getDoMainName(), newsPageService, domainByKeys.getKeyworlds()));
            }
        }
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(1000);
                return "2";
            }
        };
        Future<String> submit = threadPoolExecutor.submit(callable);
        try {
            String s = submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("爬取新闻类，插入到数据库完成");
//        if(resultList.size() <= 0){
//            PageHelper.startPage(pageNum, pageSize);
//            resultList = newsPageService.findNewsPage(keys);
//        }
        if(resultList.size() <= 0){
            resultList = newsPageService.findNewsPage(keys);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<NewsPage> result = targetService.findTarget(keys);
//        ThreadPoolExecutor threadPoolExecutorOne = new ThreadPoolExecutor(10, 10, 3,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20000),
//                new ThreadPoolExecutor.DiscardOldestPolicy());
        if(result.size() <= 0){
            System.out.println("开始爬取具体新闻类url");
            for(NewsPage newsPage : resultList){
                //threadPoolExecutorOne.execute(new SpiderTarget(newsPage.getNewsUrl(), targetService, newsPage.getKeyworlds()));
                threadPoolExecutor.execute(new SpiderTarget(newsPage.getNewsUrl(), targetService, newsPage.getKeyworlds()));
            }

        }
        Callable callableOne = new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(1000);
                return "2";
            }
        };
        //Future<String> submitOne = threadPoolExecutorOne.submit(callableOne);
        Future<String> submitOne = threadPoolExecutor.submit(callableOne);
        try {
            String s = submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("具体新闻类url爬取完毕");
        if(result.size() <= 0){
            PageHelper.startPage(pageNum, pageSize);
            result = newsPageService.findNewsPage(keys);
        }
        System.out.println(result.size());
        System.out.println("取到具体新闻类url完成");
        //System.out.println(resultList.size());
        //PageHelper.startPage(pageNum, pageSize);
        com.github.pagehelper.PageInfo pageInfo = new com.github.pagehelper.PageInfo(result, 5);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageInfo", pageInfo);
        return jsonObject;
    }

    /**
     * 根据关键字url爬取具体信息
     *
    **/
    @RequestMapping(value="PageByKey")
    public JSONObject selectPageInfoByKey(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "key") String key){
        PageHelper.startPage(pageNum, pageSize);
        List<PageInfo> list = pageInfoService.selectPageInfoByKey(key);
        List<PageInfo> results = new ArrayList<>();
        boolean flag = false;
        if(list.size() <= 0){
            List<NewsPage> listNews = targetService.findTarget(key);
            if(listNews.size() <= 0){
                flag = true;
            }else{
                for(int i=0; i<listNews.size(); i++){
                    List<PageInfo> pageInfos = pageInfoService.selectPageInfoByUrl(listNews.get(i).getNewsUrl(), key, null);
                    if(pageInfos.size() > 0){
                        PageInfo pageInfo = pageInfos.get(0);
                        pageInfo.setHtmlSource(ReadFile.readFile(ss + pageInfo.getHtmlSource()).replaceAll(">", "~"));
                        pageInfo.setContent(ReadFile.readFile(ss + pageInfo.getContent()));
                        results.add(pageInfo);
                    }
                }
                PageHelper.startPage(pageNum, pageSize);
                results = pageInfoService.selectPageInfoByKey(key);
            }
        }
        com.github.pagehelper.PageInfo pageInfo = new com.github.pagehelper.PageInfo(results, 10);
        JSONObject jsonObject = new JSONObject();
        if(flag == true){
            jsonObject.put("errmessage", "请先爬取具体网页url");
        }else{
            jsonObject.put("pageInfo", pageInfo);
        }
        return jsonObject;
    }


}
