package com.rostov.transport.telegrambot.service.telegram.impl;

import static com.rostov.transport.telegrambot.contants.CacheConstants.ALERT_CACHE_NAME;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_ALERT_DATA;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_COMMAND_NEW_ALERT;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_COMMAND_START;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_HELLO;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_BUS;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_DAY;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_END_TIME;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_START_TIME;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_NEW_ALERT_START;
import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_RECEIVE_MESSAGE;
import static com.rostov.transport.telegrambot.utils.AppUtils.format;
import static com.rostov.transport.telegrambot.utils.AppUtils.parseTime;
import static java.lang.Math.toIntExact;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

import com.rostov.transport.telegrambot.model.Alert;
import com.rostov.transport.telegrambot.model.User;
import com.rostov.transport.telegrambot.service.core.AlertService;
import com.rostov.transport.telegrambot.service.core.UserService;
import com.rostov.transport.telegrambot.service.telegram.TelegramBotReceiveMessageService;
import com.rostov.transport.telegrambot.service.telegram.TelegramBotSendMessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Slf4j
@Service
@CacheConfig(cacheNames = {ALERT_CACHE_NAME})
public class TelegramBotReceiveMessageServiceImpl implements TelegramBotReceiveMessageService {

    private final TelegramBotSendMessageService telegramBotSendMessageService;
    private final AlertService alertService;
    private final UserService userService;
    private final Cache alertCache;

    public TelegramBotReceiveMessageServiceImpl(
            TelegramBotSendMessageService telegramBotSendMessageService,
            CacheManager cacheManager,
            AlertService alertService,
            UserService userService) {

        this.telegramBotSendMessageService = telegramBotSendMessageService;
        this.alertService = alertService;
        this.userService = userService;
        this.alertCache = cacheManager.getCache(ALERT_CACHE_NAME);
    }

    @Override
    public void receiveMessage(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasText() && message.getReplyToMessage() == null) {
                var messageText = message.getText();

                log.debug(TELEGRAM_BOT_RECEIVE_MESSAGE, message.getMessageId(), messageText);

                if (messageText.equals(TELEGRAM_BOT_COMMAND_START)) {

                    if (!userService.existsByTgChatId(message.getChatId())) {
                        userService.createUser(
                                User.builder()
                                        .tgChatId(message.getChatId())
                                        .userName(message.getChat().getFirstName())
                                        .userNick(message.getChat().getUserName())
                                        .build()
                        );
                    }

                    telegramBotSendMessageService.sendMessage(
                            generateSendMessage(message.getChatId(),
                                    format(TELEGRAM_BOT_MSG_HELLO, message.getChat().getFirstName())
                            )
                    );
                }

                if (messageText.equals(TELEGRAM_BOT_COMMAND_NEW_ALERT)) {
                    var sendMessage = generateSendMessage(message.getChatId(),
                            TELEGRAM_BOT_MSG_NEW_ALERT_START);
                    setForceReplyToMessage(sendMessage);

                    alertCache.put(message.getChatId(), Alert.builder()
                            .id(UUID.randomUUID())
                            .userId(userService.getUserByTgChatId(message.getChatId()).getId())
                            .build()
                    );

                    log.debug(TELEGRAM_BOT_ALERT_DATA,
                            alertCache.get(message.getChatId(), Alert.class));
                    telegramBotSendMessageService.sendMessage(sendMessage);
                }

                // TODO delete test command
//                if (messageText.equals("/rtk")) {
//                    var sendMessage = generateSendMessage(message.getChatId(), "Введите время начала оповещений");
//                    generateReplyKeyboard(sendMessage);
//                    telegramBotSendMessageService.sendMessage(sendMessage);
//                }

            } else if (message.getReplyToMessage() != null) {
                if (message.hasText()) {
                    var reply = message.getReplyToMessage();
                    if (reply.getText().equals(TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_BUS)) {
                        alertCache.put(message.getChatId(),
                                alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                        .buses(List.of(message.getText()))
                                        .build());
                        var sendMessage = generateSendMessage(message.getChatId(),
                                TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_DAY);
                        setForceReplyToMessage(sendMessage);
                        telegramBotSendMessageService.sendMessage(sendMessage);

                    } else if (reply.getText().equals(TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_DAY)) {

                        alertCache.put(message.getChatId(),
                                alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                        .days(List.of(Integer.valueOf(message.getText())))
                                        .build());
                        var sendMessage = generateSendMessage(message.getChatId(),
                                TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_START_TIME);
                        setForceReplyToMessage(sendMessage);
                        telegramBotSendMessageService.sendMessage(sendMessage);

                    } else if (reply.getText()
                            .equals(TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_START_TIME)) {

                        alertCache.put(message.getChatId(),
                                alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                        .startTime(parseTime(message.getText()))
                                        .build());

                        var sendMessage = generateSendMessage(message.getChatId(),
                                TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_END_TIME);
                        setForceReplyToMessage(sendMessage);
                        telegramBotSendMessageService.sendMessage(sendMessage);

                    } else if (reply.getText().equals(TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_END_TIME)) {

                        alertCache.put(message.getChatId(),
                                alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                        .endTime(parseTime(message.getText()))
                                        .build());

                        var sendMessage = generateSendMessage(message.getChatId(), "The end");
                        telegramBotSendMessageService.sendMessage(sendMessage);

                        log.debug(TELEGRAM_BOT_ALERT_DATA,
                                alertCache.get(message.getChatId(), Alert.class));
                        alertService.createAlert(alertCache.get(message.getChatId(), Alert.class));
                    }
                }
                if (message.hasLocation()) {
                    var reply = message.getReplyToMessage();
                    if (reply.getText().equals(TELEGRAM_BOT_MSG_NEW_ALERT_START)) {
                        var location = message.getLocation();

                        alertCache.put(message.getChatId(),
                                alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                        .latitude(location.getLatitude())
                                        .longitude(location.getLongitude())
                                        .build()
                        );

                        log.debug(TELEGRAM_BOT_ALERT_DATA,
                                alertCache.get(message.getChatId(), Alert.class));

                        var sendMessage = generateSendMessage(message.getChatId(),
                                TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_BUS);

                        setForceReplyToMessage(sendMessage);
                        telegramBotSendMessageService.sendMessage(sendMessage);
                    }

                }

            }
        } else if (update.hasCallbackQuery()) {
            // Set variables
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callData.equals("update_msg_text")) {
                String answer = "Updated message text";
                EditMessageText newMessage = new EditMessageText(answer);
                newMessage.setChatId(chatId);
                newMessage.setMessageId(toIntExact(messageId));
                telegramBotSendMessageService.execute(newMessage);
            }
        }
    }

    private void generateReplyKeyboard(SendMessage sendMessage) {
        List<KeyboardRow> rows = new ArrayList<>();
        var row = new KeyboardRow();
        row.add("123");
        KeyboardButton btn = new KeyboardButton("457457");
        row.add(btn);
        rows.add(row);
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        sendMessage.setReplyMarkup(markup);
    }

    private SendMessage generateSendMessage(long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .parseMode(HTML)
                .disableWebPagePreview(true)
                .text(text)
                .build();
    }

    private InlineKeyboardButton generateInlineButton(String buttonText,
            String buttonCallbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
        button.setCallbackData(buttonCallbackData);
        return button;
    }

    private SendMessage setInlineButtonToMessage(SendMessage message,
            List<InlineKeyboardRow> inlineButtons) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup(inlineButtons);
        message.setReplyMarkup(markupInline);
        return message;
    }

    private void setForceReplyToMessage(SendMessage message) {
        ForceReplyKeyboard forceReply = new ForceReplyKeyboard();
        forceReply.setForceReply(true);
        message.setReplyMarkup(forceReply);
    }
}
