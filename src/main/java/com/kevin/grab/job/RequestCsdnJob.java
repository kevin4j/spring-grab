package com.kevin.grab.job;

import com.kevin.grab.utils.HttpClientUtils;
import com.kevin.grab.utils.CsdnUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestCsdnJob {

    private static final Logger logger = LoggerFactory.getLogger(RequestCsdnJob.class);

//    @Scheduled(cron="20 0/2 * * * ?")
    public void getReadNum(){

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("cookie", CsdnUtils.getCookie());

        String html = HttpClientUtils.get("https://blog.csdn.net/guangcaiwudong/article/details/95998691");

        // 将html解析成DOM结构，获取当前阅读数
        logger.info("当前"+ CsdnUtils.parseReadNum(html));
    }
}
