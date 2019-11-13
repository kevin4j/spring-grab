package com.kevin.grab.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 浏览器自动化测试工具
 * 需要指定浏览器驱动路径, 本例提供驱动在src/main/webdriver下，可移动到指定的目录
 * 驱动的安装可参考：http://www.testclass.net/selenium_java/selenium3-browser-driver
 */
public class WebUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    private static WebDriver driver = null;

    static{
        //System.setProperty("webdriver.gecko.driver","D:/software/webdriver/geckodriver.exe");
        try {
            System.setProperty("webdriver.gecko.driver",new ClassPathResource("webdriver/geckodriver.exe").getFile().getAbsolutePath());

            FirefoxOptions options=new FirefoxOptions();
            options.setHeadless(true);
            driver = new FirefoxDriver(options);

//        System.setProperty("webdriver.chrome.driver","D:/software/webdriver/chromedriver.exe");
//        ChromeOptions options=new ChromeOptions();
//        options.setHeadless(true);
//        List<String> excludeSwitches = new ArrayList<String>();
//        excludeSwitches.add("enable-automation");
//        options.setExperimentalOption("excludeSwitches", excludeSwitches);
//        driver = new ChromeDriver(options);
        }catch (Exception e){
            logger.error("加载浏览器驱动出现异常",e);
        }

    }

    public static WebDriver getDriver(){
        return driver;
    }

    public static String get(String url){
        try{
            driver.get(url);

            Thread.sleep(5000);
            return driver.getPageSource();
        }catch (Exception e){
            logger.error("请求"+url+"出错",e);
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new ClassPathResource("webdriver/geckodriver.exe").getFile().getAbsolutePath());
    }
}
