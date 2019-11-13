package com.kevin.grab.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class CsdnUtils {

    public static String cookieStr=null;

    public static String getCookie(){
        if(StringUtils.isBlank(cookieStr)){
            return refreshCookie();
        }
        return cookieStr;
    }

    public static String refreshCookie(){
        cookieStr = CsdnUtils.login();
        return cookieStr;
    }

    /**
     * 登录
     * @return
     */
    public static String login(){
        String url="https://passport.csdn.net/v1/register/pc/login/doLogin";

        Map<String, String> params=new HashMap<>();
        params.put("loginType", "1");
        params.put("userIdentification","");
        params.put("pwdOrVerifyCode","");

        Map<String, String> headers=new HashMap<>();
        headers.put("Content-Type","application/json");

        Header[]  respHeaders=HttpClientUtils.postReturnHeader(url, JSONObject.toJSONString(params));
        StringBuffer cookieBuffer=new StringBuffer();
        if(respHeaders!=null){
            for (Header respHeader : respHeaders) {
                if(StringUtils.equals(respHeader.getName(),"Set-Cookie")){
                    String headerValue=respHeader.getValue();
                    String[] valueArr=headerValue.split(";");
                    if(valueArr.length>0){
                        cookieBuffer.append(valueArr[0]).append(";");
                    }
                }
            }
        }

        System.out.println("登录CSDN，当前coolie为："+cookieBuffer.toString());

        return cookieBuffer.toString();
    }

    /**
     * 获取当前阅读数
     * @param html
     * @return
     */
    public static String parseReadNum(String html){
        // 将html解析成DOM结构，获取当前阅读数
        Document document = Jsoup.parse(html);
        return parseReadNum(document);
    }

    public static String parseReadNum(Document document){
        Elements trs = document.select("div[id=mainBox]").select("span[class=read-count]");
        if(trs!=null && trs.size()>0){
            return trs.get(0).text();
        }
        return "";
    }

}
