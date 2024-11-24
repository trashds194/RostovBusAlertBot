package com.rostov.transport.telegrambot.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application")
public record ApplicationProperties(
        @NotNull BusApiProperties busApi,
        @NotNull TelegramBotProperties telegrambots
) {
}
