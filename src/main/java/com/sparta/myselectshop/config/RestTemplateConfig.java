package com.sparta.myselectshop.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/*
    수동으로 Bean 객체로 등록하는 방법
    @Configuration, @Bean
 */
@Configuration
public class RestTemplateConfig  {
    /*
        외부에 api를 요청하는데 응답시간과 대기시간에 제한을 걸어두는 설정
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                // RestTemplate 으로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 때
                // 무한 대기 상태 방지를 위해 강제 종료 설정
                .setConnectTimeout(Duration.ofSeconds(5)) // 5초
                .setReadTimeout(Duration.ofSeconds(5)) // 5초
                .build();
    }
}