package com.rostov.transport.telegrambot.service.impl;

import com.rostov.transport.telegrambot.bot.BotComponent;
import com.rostov.transport.telegrambot.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final BotComponent bot;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        bot.sendMessage(sendMessage);
    }
}
