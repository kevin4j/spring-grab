package com.kevin.grab.job;

import com.alibaba.fastjson.JSONObject;
import com.kevin.grab.utils.CsdnUtils;
import com.kevin.grab.utils.HttpClientUtils;
import com.kevin.grab.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.*;

@Component
public class RequestBaiduJob {

    private static final Logger logger = LoggerFactory.getLogger(RequestBaiduJob.class);

    public static Map<String, String> keywordMap=new HashMap<>();

    static {
        //格式------百度关键词keyword : 链接title过滤词filter word
//        keywordMap.put("微服务入门学习", "guangcai");
        keywordMap.put("spring guangcaiwudong", "");
    }

//    @Scheduled(cron="30 0/3 * * * ?")
    public static void searchAll()throws  Exception{
        Iterator<String> it=keywordMap.keySet().iterator();
        while (it.hasNext()){
            String key=it.next();
            searchByWebDriver(key, keywordMap.get(key));
        }
    }

    public static void search(String keyword, String filterWord) throws  Exception{

        Map<String, String> headerMap = new HashMap<>();

        keyword = URLEncoder.encode(keyword, "utf-8");
        for(int p=0;p<4;p++){
            String url="https://www.baidu.com/s?wd="+keyword+"&pn="+p*10+"&oq="+keyword+"&ie=utf-8&usm=1&rsv_idx=1&rsv_pq=83c2f68c00060025&rsv_t=bdff%2BF64nG%2F9old%2B2dDmlxkLvcdQLVYCknDFL3%2BTdrxyh%2FET2WT0bgJnmvs";
            String html = HttpClientUtils.get(url);

            Document document = Jsoup.parse(html);
            Elements elems = document.select("div[id=content_left]").select("div.result");
            if(elems!=null && elems.size()>0){
                for(int i = 0 ; i< elems.size(); i++){
                    String data=elems.get(i).select("div.f13").select("div.c-tools").attr("data-tools");
                    try{
                        if(StringUtils.isNotEmpty(data)){
                            Map<String, String> dataMap=JSONObject.parseObject(data, Map.class);
                            String[] fileWordArr=filterWord.split(",");
                            for (String word : fileWordArr){
                                if(dataMap.get("title").contains(word)){
                                    logger.info(data);

                                    String csdnHtml = HttpClientUtils.get(dataMap.get("url"), headerMap);
                                    logger.info("当前"+ CsdnUtils.parseReadNum(csdnHtml));
                                    break;
                                }
                            }
                        }
                    }catch (Exception e){
                        logger.error("解析出错："+data);
                    }
                }

                if(elems.size()<5){
                    break;
                }
            }
        }

    }

    /**
     * 使用浏览器驱动，自动化测试打开页面
     * @param keyword
     * @param filterWord
     * @throws Exception
     */
    public static void searchByWebDriver(String keyword, String filterWord) throws  Exception{

        Map<String, String> headerMap = new HashMap<>();

        keyword = URLEncoder.encode(keyword, "utf-8");
        for(int p=0;p<4;p++){
            String url="https://www.baidu.com/s?wd="+keyword+"&pn="+p*10+"&oq="+keyword+"&ie=utf-8&usm=1&rsv_idx=1&rsv_pq=83c2f68c00060025&rsv_t=bdff%2BF64nG%2F9old%2B2dDmlxkLvcdQLVYCknDFL3%2BTdrxyh%2FET2WT0bgJnmvs";
            WebDriver driver= WebUtils.getDriver();
            driver.get(url);
            Thread.sleep(300);

            //WebElement link = driver.findElement(By.linkText("guangcaiwudong"));
            List<WebElement> linkList = driver.findElements(By.ByPartialLinkText.partialLinkText("guangcaiwudong"));
            for (WebElement elem : linkList){
                elem.click();
                Thread.sleep(1000);
            }

            Thread.sleep(1000);
            //获取当前页面句柄
            String handle = driver.getWindowHandle();
            List<String> handleList=new ArrayList<>(driver.getWindowHandles());
            Collections.reverse(handleList);
            for (String temhandle : handleList) {
                driver.switchTo().window(temhandle);
                if(!StringUtils.equals(temhandle, handle)){

                    if(driver.findElements(By.className("title-article")).size()>0) {
                        logger.debug(driver.findElement(By.className("title-article")).getText() + "," + driver.findElement(By.className("read-count")).getText());
                    }
                    logger.debug("关闭页面"+driver.getCurrentUrl());
                    driver.close();
                }
                Thread.sleep(1000);
            }
        }
    }

    public static void main(String[] args) throws  Exception{
        while (true){
            searchAll();
            Thread.sleep(20000);
        }
    }
}
