package com.rostov.transport.telegrambot.service.telegram.impl;

import static com.rostov.transport.telegrambot.contants.MessagesConstants.SENDING_MESSAGE;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.SENDING_MESSAGE_ERROR;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.SENDING_MESSAGE_SUCCESSFUL;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_EXECUTED_DATA;

import com.rostov.transport.telegrambot.service.telegram.TelegramBotSendMessageService;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotSendMessageServiceImpl implements TelegramBotSendMessageService {

    private final TelegramClient telegramClient;

    @Override
    public CompletableFuture<Message> sendMessage(SendMessage sendMessage) {
        log.info(SENDING_MESSAGE, sendMessage.getChatId());

        try {
            return telegramClient.executeAsync(sendMessage).handle(this::getResultMessage);

        } catch (TelegramApiException ex) {

            log.error(SENDING_MESSAGE_ERROR, sendMessage.getChatId(), ex.getMessage(), ex);
            return CompletableFuture.failedFuture(ex);
        }
    }

    @Override
    public BotApiObject execute(EditMessageText editMessageText) {
        try {
            var botApiObject = (BotApiObject) telegramClient.execute(editMessageText);
            log.debug(TELEGRAM_BOT_EXECUTED_DATA, botApiObject);
            return botApiObject;
        } catch (TelegramApiException ex) {
            log.error(SENDING_MESSAGE_ERROR, editMessageText.getChatId(), ex.getMessage(), ex);
            return null;
        }
    }

    private Message getResultMessage(Message message, Throwable throwable) {
        if (Objects.nonNull(throwable)) {
            log.error(SENDING_MESSAGE_ERROR, message.getChatId(), throwable.getMessage(),
                    throwable);
            return null;
        }

        log.debug(SENDING_MESSAGE_SUCCESSFUL, message.getChatId(), message);
        return message;
    }
}
