package com.rostov.transport.telegrambot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TemplateConfig {

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }
}
