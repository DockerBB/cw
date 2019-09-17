package csu.cw.controller;

import csu.cw.entity.PageInfo;
import csu.cw.service.PageInfoService;
import csu.cw.util.ReadFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    PageInfoService pageInfoService;

    private final static String ss = "E:\\source";

    //private final static String prePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "source" + System.getProperty("file.separator");

    /**
     * 文件上传
     * **/
    @RequestMapping(value="upload")
    public String upload(@RequestParam("file")MultipartFile file, HttpServletResponse response){
        try {
            if(file.isEmpty()){
                return "文件为空";
            }
            System.out.println("接受到此文件");
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            String result = new String(bytes, "UTF-8");
            String[] splits = result.split("\r\n");
            List<String> list = new ArrayList<>();
            for(int i=0; i<splits.length; i++){
                list.add(splits[i]);
            }
            List<PageInfo> results = new ArrayList<>();
            for(int i=0; i<list.size(); i++){
                List<PageInfo> pageInfos = pageInfoService.selectPageInfoByUrl(list.get(i), "", null);
                if(pageInfos.size() > 0){
                    //Pageresults.add(pageInfos.get(0));
                    PageInfo pageInfo = pageInfos.get(0);
                    pageInfo.setHtmlSource(ReadFile.readFile(ss + pageInfo.getHtmlSource()).replaceAll(">", "~"));
                    pageInfo.setContent(ReadFile.readFile(ss + pageInfo.getContent()));
                    results.add(pageInfo);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            JSONArray json = JSONArray.fromObject(results);
            //out.println(json);
            String json_str = json.toString();
            out.print(json_str);
            out.flush();
            out.close();
            return "";
            //String filePath = "";
            //String path = filePath + fileName;
            /**
            String prePath = "E:\\file\\source\\";
            String path = prePath + fileName;
            File dest = new File(path);
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
             **/
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("文件上传成功");
        return "上传失败";
    }


    @RequestMapping(value="PageByKey")
    public JSONObject selectPageInfoByKey(@RequestParam(value="pageNum",defaultValue = "1") int pageNum, @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "key") String key){

        return null;
    }




}
