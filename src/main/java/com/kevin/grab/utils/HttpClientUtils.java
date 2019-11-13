package com.kevin.grab.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    public static String get(String url){
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headerMap){
        HttpGet httpGet = new HttpGet(url);

        if(headerMap!=null){
            Iterator<String> keyIt=headerMap.keySet().iterator();
            while(keyIt.hasNext()){
                String headerName = keyIt.next();
                httpGet.setHeader(headerName,headerMap.get(headerName));
            }
        }

        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        return getContent(url, httpGet);
    }

    private static String getContent(String url, HttpGet httpGet){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response=null;
        String result=null;
        try {
            response = httpclient.execute(httpGet);
            logger.info("请求 {}，代理{}，返回状态：{}",url,httpGet.getConfig() ==null ||  httpGet.getConfig().getProxy() == null ? "" : httpGet.getConfig().getProxy().toString(),response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();

            result=EntityUtils.toString(entity, "UTF-8");

//            logger.info(result);

            // 释放资源
            EntityUtils.consume(entity);
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if(response!=null){
                try {
                    response.close();
                    httpclient.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 使用IP代理Get请求
     * @param url   请求地址
     * @param ip    代理IP
     * @param port  代理端口
     * @return  返回内容
     */
    public static String getProxy(String url, String ip, Integer port){
        return getProxy(url, ip, port, null);
    }

    public static String getProxy(String url, String ip, Integer port, Map<String, String> headerMap){

        HttpGet httpGet = new HttpGet(url);

        RequestConfig.Builder builder = RequestConfig.custom();

        // 设置代理访问
        if(!StringUtils.isEmpty(ip)){
            HttpHost proxy = new HttpHost(ip, port);
            builder.setProxy(proxy);

            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        }

        if(headerMap!=null){
            Iterator<String> keyIt=headerMap.keySet().iterator();
            while(keyIt.hasNext()){
                String headerName = keyIt.next();
                httpGet.setHeader(headerName,headerMap.get(headerName));
            }
        }

        // 设置超时
        RequestConfig config =builder.setConnectTimeout(10000).setSocketTimeout(5000).build();
        httpGet.setConfig(config);

        return getContent(url, httpGet);

    }

    public static String post(String url, String body) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(body));

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String result=null;
        try {
            logger.info(JSONObject.toJSONString(response.getAllHeaders()));
            HttpEntity entity = response.getEntity();
            result=EntityUtils.toString(entity, "UTF-8");

            logger.info(result);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return result;

    }

    /**
     * POST请求返回header
     * @param url
     * @param body
     * @return
     */
    public static Header[] postReturnHeader(String url, String body){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpResponse response=null;
        Header[] result=null;
        try {
            httpPost.setEntity(new StringEntity(body));

            response = httpclient.execute(httpPost);

            if(response.getStatusLine().getStatusCode()==200){
                result=response.getAllHeaders();
            }else{
                logger.error("HttpPost请求返回"+EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
        }catch (Exception e){
            logger.error("HttpPost请求出现异常"+e.getMessage(), e);
        }finally {
            try{
                if(response!=null){
                 response.close();
                }
                httpclient.close();
            }catch (IOException e){
                logger.error("HttpPost请求释放资源出现异常"+e.getMessage(), e);
            }
        }
        return result;

    }

    public static String post(String url, Map<String, String> params, Map<String, String> headerMap) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        if(params!=null){
            Iterator<String> keyIt=params.keySet().iterator();
            while(keyIt.hasNext()){
                String headerName = keyIt.next();
                nvps.add(new BasicNameValuePair(headerName,params.get(headerName)));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));

        if(headerMap!=null){
            Iterator<String> keyIt=headerMap.keySet().iterator();
            while(keyIt.hasNext()){
                String headerName = keyIt.next();
                httpPost.setHeader(headerName,headerMap.get(headerName));
            }
        }

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String result=null;
        try {
            HttpEntity entity = response.getEntity();
            result=EntityUtils.toString(entity, "UTF-8");

            logger.info(result);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return result;

    }

    public static void main(String[] args) {

    }
}
