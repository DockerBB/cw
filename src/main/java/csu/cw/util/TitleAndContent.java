package csu.cw.util;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleAndContent {

    public static List<String> getTitleAndContent(String html){
        List<String> lists = new ArrayList<>();
        String title = "";
        String pageContent = "";
        int start = html.indexOf("<title");
        String tmp = "";
        if(start > -1){
            tmp = html.substring(start);
        }
        int end = -1;
        if(tmp.length() > 0){
            end = tmp.indexOf("/title>");
        }
        if(end > -1){
            String tmp1 = tmp.substring(tmp.indexOf(">")+1);
            title = tmp1.substring(0, tmp1.indexOf("<"));
        }
        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(html.toString().getBytes()));
            String charset = getCharSetByBody(html, "UTF-8");
            if(charset != null){
                inputSource.setEncoding(charset);
            }else{
                inputSource.setEncoding("UTF-8");
            }
            TextDocument textDocument = new BoilerpipeSAXInput(inputSource).getTextDocument();
            BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
            extractor.process(textDocument);
            if(title.length() <= 0){
                title = textDocument.getTitle();
            }
            pageContent = textDocument.getContent();
        } catch (BoilerpipeProcessingException e) {
            System.out.println("解析错误");
        } catch (SAXException e) {
            System.out.println("解析错误");
        }
        lists.add(title);
        lists.add(pageContent);
        return lists;
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

}
