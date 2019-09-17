package csu.cw.webmagic;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.swing.text.Document;
import javax.swing.text.Element;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientSpider {

    private static PoolingHttpClientConnectionManager cm;

    public static String doGet(String url){
        String charset = "UTF-8";
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        HttpGet httpGet = null;
        try{
            httpGet = new HttpGet(url);
        }catch (Exception e){
            System.out.println("连接错误");
            return "";
        }

        httpGet.setConfig(getConfig());
        httpGet.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("accept-encoding", "gzip, deflate");
        httpGet.setHeader("accept-language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");
        CloseableHttpResponse response = null;
        String content = "";
        try {
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                if(response.getEntity() != null){
                    try {
                        HttpEntity entity = response.getEntity();
                        InputStream content1 = entity.getContent();
                        byte[] responseBytes = input2byte(content1);
                        String body = new String(responseBytes, "UTF-8");
                        charset = getCharSetByBody(body, charset);
                        if(charset != null){
                            content = new String(responseBytes, charset);
                        }else{
                            content = new String(responseBytes, "UTF-8");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //content = EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    public static String getCharSetByBody(String html, String charset){
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Elements elements = document.select("meta");
        for(org.jsoup.nodes.Element metaElement : elements){
            if(metaElement != null && StringUtils.isNotBlank(metaElement.attr("http-equiv")) && metaElement.attr("http-equiv").toLowerCase().equals("content-type")){
                String content = metaElement.attr("content");
                charset = getCharSet(content);
                break;
            }
        }
        return charset;
    }

    public static String getCharSet(String content){
        String regex = ".*charset=([^;]*).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            return matcher.group(1);
        }else{
            return null;
        }
    }

    public static byte[] getData(HttpEntity httpEntity) throws Exception{
        BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bufferedHttpEntity.writeTo(byteArrayOutputStream);
        byte[] responseBytes = byteArrayOutputStream.toByteArray();
        return responseBytes;
    }

    public static byte[] input2byte(InputStream inputStream) throws Exception{
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while((rc = inputStream.read(buff, 0, 100)) > 0){
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static RequestConfig getConfig(){
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000000)
                .setConnectionRequestTimeout(50000)
                .setSocketTimeout(100000)
                .build();
        return config;
    }

}
