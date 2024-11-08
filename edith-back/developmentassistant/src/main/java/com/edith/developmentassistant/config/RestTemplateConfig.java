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

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Qualifier("userServiceRestTemplate")
    public RestTemplate userServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Qualifier("ragRestTemplate")
    public RestTemplate ragRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Qualifier("portfolioRestTemplate")
    public RestTemplate portfolioRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
