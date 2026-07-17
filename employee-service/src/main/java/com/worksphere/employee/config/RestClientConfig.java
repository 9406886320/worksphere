package com.worksphere.employee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${department.service.url}")
    private String departmentServiceUrl;

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .baseUrl(departmentServiceUrl)
                .build();
    }
}