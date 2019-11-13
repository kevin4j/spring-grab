package com.kevin.grab.ip;

import com.alibaba.fastjson.JSONObject;
import com.kevin.grab.ip.model.IPInfo;
import com.kevin.grab.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;

public class IPCollector {

    public static Map<String, String> proxyWebSiteUrl = new HashMap<>();

    static{
        proxyWebSiteUrl.put("xicidaili", "https://www.xicidaili.com/nn/1");
        proxyWebSiteUrl.put("kuaidaili", "https://www.kuaidaili.com/free/inha/1");
    }

    /**
     * 获取所有代理IP
     * @return
     */
    public static List<IPInfo> getIPList(){
        Iterator<String> proxyNameIt=proxyWebSiteUrl.keySet().iterator();
        List<IPInfo> ipList=new ArrayList<>();
        while (proxyNameIt.hasNext()){
            ipList.addAll(getIPListByProxy(proxyNameIt.next()));
        }
        return ipList;
    }

    /**
     * 爬取代理网站
     * @param proxyName
     * @return
     */
    public static List<IPInfo> getIPListByProxy(String proxyName) {
        String url = proxyWebSiteUrl.get(proxyName);
        if(StringUtils.isBlank(url)){
            throw new RuntimeException("不支持该代理网站");
        }

        String html = HttpClientUtils.getProxy(url, null, null);

        // 将html解析成DOM结构
        Document document = Jsoup.parse(html);

        // 提取所需要的数据
        if(StringUtils.equals("xicidaili", proxyName)){
            return parseHtml4Xici(document);
        }else if(StringUtils.equals("kuaidaili", proxyName)){
            return parseHtml4Kuai(document);
        }

        return null;

    }

    /**
     * 解析xicidaili
     * @param document
     * @return
     */
    public static List<IPInfo> parseHtml4Xici(Document document) {
        List<IPInfo> ipInfos =new ArrayList<>();
        Elements trs = document.select("table[id=ip_list]").select("tr");
        IPInfo ipInfo;
        for (int i = 1; i < trs.size(); i++) {
            ipInfo = new IPInfo();

            ipInfo.setAddress(trs.get(i).select("td").get(1).text());
            ipInfo.setPort(trs.get(i).select("td").get(2).text());
            ipInfo.setType(trs.get(i).select("td").get(5).text());
            ipInfo.setSpeed(trs.get(i).select("td").get(6).select("div[class=bar]").attr("title"));

            ipInfos.add(ipInfo);
        }

        return ipInfos;
    }

    /**
     * 解析kuaidaili
     * @param document
     * @return
     */
    public static List<IPInfo> parseHtml4Kuai(Document document) {
        List<IPInfo> ipInfos =new ArrayList<>();
        Elements trs = document.select("div[id=list]").select("table").select("tbody").select("tr");
        IPInfo ipInfo;
        for (int i = 0; i < trs.size(); i++) {
            ipInfo = new IPInfo();

            ipInfo.setAddress(trs.get(i).select("td").get(0).text());
            ipInfo.setPort(trs.get(i).select("td").get(1).text());
            ipInfo.setType(trs.get(i).select("td").get(3).text());
            ipInfo.setSpeed(trs.get(i).select("td").get(5).text());

            ipInfos.add(ipInfo);
        }

        return ipInfos;
    }

    public static void main(String[] args) {
        System.out.println(JSONObject.toJSON(getIPList()));
    }


}
