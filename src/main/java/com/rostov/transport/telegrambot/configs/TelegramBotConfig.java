package com.rostov.transport.telegrambot.configs;

import com.rostov.transport.telegrambot.common.TelegramBot;
import com.rostov.transport.telegrambot.properties.ApplicationProperties;
import com.rostov.transport.telegrambot.service.telegram.TelegramBotReceiveMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

    @Bean
    public TelegramClient telegramClient(ApplicationProperties properties) {
        return new OkHttpTelegramClient(properties.telegrambots().botToken());
    }

    @Bean
    public TelegramBot telegramBot(
            TelegramBotReceiveMessageService telegramBotReceiveMessageService) {
        return new TelegramBot(telegramBotReceiveMessageService);
    }

    @Bean
    public TelegramBotsLongPollingApplication registerBot(ApplicationProperties properties,
            TelegramBot telegramBot) throws TelegramApiException {
        TelegramBotsLongPollingApplication botApplication = new TelegramBotsLongPollingApplication();
        botApplication.registerBot(properties.telegrambots().botToken(), telegramBot);
        return botApplication;
    }
}
