package com.rostov.transport.telegrambot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramBotService {
    void sendMessage(SendMessage sendMessage);
}
