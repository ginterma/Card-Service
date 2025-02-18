package com.Gintaras.tcgtrading.card_service.WebClientConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class WebClientConfig {

    @Value("${user.service.url}")
    private String userServiceUrl;


    @Bean("user")
    public WebClient userService(WebClient.Builder builder) {
        return builder.baseUrl(userServiceUrl).build();
    }

}