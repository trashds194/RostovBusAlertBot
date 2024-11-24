package com.rostov.transport.telegrambot.service.telegram;

import java.util.concurrent.CompletableFuture;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface TelegramBotSendMessageService {

    CompletableFuture<Message> sendMessage(SendMessage sendMessage);

    BotApiObject execute(EditMessageText editMessageText);
}
