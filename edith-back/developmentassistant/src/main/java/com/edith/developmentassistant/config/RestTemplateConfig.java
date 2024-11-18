package com.edith.developmentassistant.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    private static final int TIMEOUT = 600000;

    @Bean
    @Qualifier("gitLabRestTemplate")
    public RestTemplate gitLabRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT);    // 30초
        factory.setReadTimeout(TIMEOUT);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Qualifier("userServiceRestTemplate")
    public RestTemplate userServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(600000);    // 30초
        factory.setReadTimeout(600000);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Qualifier("ragRestTemplate")
    public RestTemplate ragRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT);    // 30초
        factory.setReadTimeout(TIMEOUT);      // 10분
        RestTemplate restTemplate = new RestTemplate(factory);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Qualifier("portfolioRestTemplate")
    public RestTemplate portfolioRestTemplate() {
        // 타임아웃 설정을 위한 Factory 생성
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT);    // 30초
        factory.setReadTimeout(TIMEOUT);      // 10분

        // RestTemplate 생성 시 factory 적용
        RestTemplate restTemplate = new RestTemplate(factory);

        // 기존 인터셉터 설정 유지
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
