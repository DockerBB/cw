package csu.cw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorTime {

    private static String YearMonthDayRegexOne = "(?:January|February|March|April|May|June|July|August|" +
            "September|October|November|December|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec|JANUARY|FEBRUARY|MARCH|" +
            "APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEPT|OCT|NOV|" +
            "DEC).{0,3}(\\d{1,2})?.{0,2}\\d{2,4}";
    private static String YearMonthDayRegexTwo = "\\d{1,2}.{1,2}(?:January|February|March|April|May|June|July|August|" +
            "September|October|November|December|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec|JANUARY|FEBRUARY|MARCH|" +
            "APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEPT|OCT|NOV|" +
            "DEC).{1,3}\\d{2,4}";
    private static String YearMonthDayRegexThree = "(\\s*\\d{2,4}[年\\-/])?(\\d{1,2}[月\\-/]){1}\\d{1,2}[日]?";

    public static String extractorTime(String htmlSource, String title){
        String pageContent = getPageContent(htmlSource).trim();
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(pageContent);
        pageContent = m.replaceAll("");
        String pagecontentDel = pageContent.substring(2);
        int flag = getIndex(pagecontentDel, title);
        String YearMonthDay = null;
        if(flag != -1){
            String deleteTitleContent = pagecontentDel.substring(flag);
            YearMonthDay = getDataTarget(deleteTitleContent, YearMonthDayRegexOne);
            if(YearMonthDay == null){
                YearMonthDay = getDataTarget(deleteTitleContent, YearMonthDayRegexTwo);
                if(YearMonthDay == null){
                    YearMonthDay = getDataTarget(deleteTitleContent, YearMonthDayRegexThree);
                }
            }else{
                String tmpTime = getDataTarget(deleteTitleContent, YearMonthDayRegexTwo);
                if(tmpTime != null){
                    if(tmpTime.length() > YearMonthDay.length()){
                        YearMonthDay = tmpTime;
                    }
                }
            }
        }else{
            flag = getIndex(pagecontentDel, decideSpecial(title));
            if(flag != -1){
                String deleteTitleContent = pagecontentDel.substring(flag);
                YearMonthDay = getDataTarget(deleteTitleContent, YearMonthDayRegexOne);
                if(YearMonthDay == null){
                    YearMonthDay = getDataTarget(deleteTitleContent, YearMonthDayRegexTwo);
                    if(YearMonthDay == null){
                        YearMonthDay = getDataTarget(deleteTitleContent, YearMonthDayRegexThree);
                    }
                }else{
                    String tmpTimeOne = getDataTarget(deleteTitleContent, YearMonthDayRegexTwo);
                    if(tmpTimeOne != null){
                        if(tmpTimeOne.length() > YearMonthDay.length()){
                            YearMonthDay = tmpTimeOne;
                        }
                    }
                }
            }else{
                YearMonthDay = getDataTarget(pageContent, YearMonthDayRegexOne);
                if(YearMonthDay == null){
                    YearMonthDay = getDataTarget(pageContent, YearMonthDayRegexTwo);
                    if(YearMonthDay == null){
                        YearMonthDay = getDataTarget(pageContent, YearMonthDayRegexThree);
                    }
                }else{
                    String tmpTimeTwo = getDataTarget(pageContent, YearMonthDayRegexTwo);
                    if(tmpTimeTwo != null){
                        if (tmpTimeTwo.length() > YearMonthDay.length()){
                            YearMonthDay = tmpTimeTwo;
                        }
                    }
                }
            }
        }
        return YearMonthDay;
    }

    public static int getIndex(String source, String target){
        Matcher matcher = Pattern.compile(target).matcher(source);
        int flag = -1;
        if(matcher.find()){
            flag = matcher.start();
        }
        return flag;
    }

    /**
     * 根据网页源码得到文本内容
     * **/
    public static String getPageContent(String htmlStr){
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        String regEx_html = "<[^>]+>";
        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        textStr = htmlStr;
        textStr=textStr.replaceAll("[ ]+", " ");
        textStr=textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        //System.out.println(textStr);
        return textStr;
    }

    public static String getDataTarget(String source, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if(matcher.find()){
            int start = matcher.start();
            int end = matcher.end();
            return source.substring(start, end);
        }
        return null;
    }

    public static String decideSpecial(String string){
        String regex = ")";
        int flag = string.lastIndexOf(regex);
        if(flag < 0){
            String tmp = string.substring(0, string.length()/2 + 1);
            return tmp;
        }else{
            return string.substring(0, flag+1);
        }
    }

}
