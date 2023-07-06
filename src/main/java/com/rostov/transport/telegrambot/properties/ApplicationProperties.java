package com.rostov.transport.telegrambot.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties("telegrambots")
public class ApplicationProperties {

    private String botName;
    private String botToken;
}
