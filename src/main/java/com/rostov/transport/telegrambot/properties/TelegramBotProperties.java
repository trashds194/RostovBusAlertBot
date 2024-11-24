package com.rostov.transport.telegrambot.properties;

import jakarta.validation.constraints.NotNull;

public record TelegramBotProperties(
        @NotNull String botName,
        @NotNull String botToken) {

}
