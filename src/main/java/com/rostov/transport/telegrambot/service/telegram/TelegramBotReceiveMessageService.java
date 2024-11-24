package com.rostov.transport.telegrambot.service.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotReceiveMessageService {

    void receiveMessage(Update update);
}
