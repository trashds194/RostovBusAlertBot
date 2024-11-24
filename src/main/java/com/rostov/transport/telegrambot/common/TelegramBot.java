package com.rostov.transport.telegrambot.common;

import com.rostov.transport.telegrambot.service.telegram.TelegramBotReceiveMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramBotReceiveMessageService telegramBotReceiveMessageService;

    @Override
    public void consume(Update update) {
        telegramBotReceiveMessageService.receiveMessage(update);
    }
}
