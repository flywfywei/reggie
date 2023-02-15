package com.wfy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching  //启动缓存
@SpringBootApplication
public class WfyReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(WfyReggieApplication.class, args);
    }

}
