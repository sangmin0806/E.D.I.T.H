package com.edith.developmentassistant.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Qualifier("gitLabRestTemplate")
    public RestTemplate gitLabRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 예: 로깅 인터셉터 추가
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        // 추가 설정이 필요한 경우 여기에 추가

        return restTemplate;
    }

    @Bean
    @Qualifier("userServiceRestTemplate")
    public RestTemplate userServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 예: 로깅 인터셉터 추가
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        // 추가 설정이 필요한 경우 여기에 추가

        return restTemplate;
    }
}
