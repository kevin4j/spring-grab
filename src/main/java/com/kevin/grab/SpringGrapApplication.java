package com.kevin.grab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@ServletComponentScan(basePackages = {"com.kevin"})
public class SpringGrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGrapApplication.class, args);
    }

}
