package com.maxent.oms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by haiquanli on 16/6/3.
 */
@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String body = restTemplate.getForObject("https://www.baidu.com/",String.class);
        System.out.println(body);
    }
}
