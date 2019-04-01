package com.ch.hueclientinfo.Utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadJson {
    private static ReadJson readJson;
    public static ReadJson get() {
        if (readJson == null) {
            readJson = new ReadJson();
        }
        return readJson;
    }

    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\\\t|\\\\r|\\\\n|\\\\";

    public String convertJson(String json){
        String j ="";
        j = decode(json);
        j = delHTMLTag(j);
        //Log.d("ReadJson", j);
        return  j;
    }

    private   String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U'))) {

                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }
    private static String delHTMLTag(String htmlStr) {
        // 过滤script标签
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        // 过滤空格回车标签
        Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.replaceAll("&lt;", "<");
        htmlStr = htmlStr.replaceAll("&gt;", ">");
        htmlStr = htmlStr.replaceAll("subtitle\":null","subtitle\":\"\"");
        htmlStr = htmlStr.replaceAll("国家\"863\"计划", "国家“863”计划");
        htmlStr = htmlStr.replaceAll("国家\"973\"研究项目", "国家“973”研究项目");
        htmlStr = htmlStr.replaceAll("贫困村\"破茧蝶变\"", "贫困村“破茧蝶变”");
        htmlStr = htmlStr.replaceAll("提前\"扮演\"传媒人", "提前“扮演”传媒人");
        htmlStr = htmlStr.replaceAll("\"深化教育体制机制改革，加快教育现代化\"", "“深化教育体制机制改革，加快教育现代化”");
        htmlStr = htmlStr.replaceAll("\"Precision cosmology from future lensed gravitational wave" +
                " and electromagnetic signals\"", "“Precision cosmology from future lensed " +
                "gravitational wave and electromagnetic signals”");
        htmlStr = htmlStr.replace("\"五个思政\"","'五个思政'");
        htmlStr = htmlStr.replace("\"时代楷模\"","'时代楷模'");
        String str = htmlStr;
        String str2 = "";
        String[] str1 = str.split(" ");
        for (int i = 0; i < str1.length; i++) {
            if (str1[i] != "") {
                str2 += str1[i];
            }
        }
        return str2.trim();
    }

}
