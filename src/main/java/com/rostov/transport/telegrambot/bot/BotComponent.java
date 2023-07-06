package com.rostov.transport.telegrambot.bot;

import com.rostov.transport.telegrambot.gateway.BusGateway;
import com.rostov.transport.telegrambot.model.Alert;
import com.rostov.transport.telegrambot.model.User;
import com.rostov.transport.telegrambot.properties.ApplicationProperties;
import com.rostov.transport.telegrambot.service.AlertService;
import com.rostov.transport.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.rostov.transport.telegrambot.contants.BotCommandConstants.NEW_ALERT_COMMAND;
import static com.rostov.transport.telegrambot.contants.BotCommandConstants.START_COMMAND;
import static com.rostov.transport.telegrambot.contants.BotMessageConstants.*;
import static java.lang.Math.toIntExact;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Slf4j
@Component
@CacheConfig(cacheNames = {"alert"})
public class BotComponent extends TelegramLongPollingBot {

    private final ApplicationProperties properties;
    private final UserService userService;
    private final AlertService alertService;
    private final BusGateway busGateway;
    private final CacheManager cacheManager;

    //TODO
    @Lazy
    public BotComponent(ApplicationProperties properties,
                        UserService userService,
                        AlertService alertService,
                        BusGateway busGateway,
                        CacheManager cacheManager) {
        super(properties.getBotToken());
        this.properties = properties;
        this.userService = userService;
        this.alertService = alertService;
        this.busGateway = busGateway;
        this.cacheManager = cacheManager;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Cache alertCache = cacheManager.getCache("alert");
        if (alertCache == null) {
            log.error("Null cache");
        }
        if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasText() && message.getReplyToMessage() == null) {
                var messageText = message.getText();
                log.info("MessageId: {} \n MessageText: {}", message.getMessageId(), messageText);
                if (messageText.equals(START_COMMAND)) {
                    userService.createUser(
                            User.builder()
                                    .tgChatId(message.getChatId())
                                    .userName(message.getChat().getFirstName())
                                    .userNick(message.getChat().getUserName())
                                    .build()
                    );

                    sendMessage(generateSendMessage(
                            message.getChatId(),
                            String.format(HELLO_MSG, message.getChat().getFirstName()))
                    );
                }
                if (messageText.equals(NEW_ALERT_COMMAND)) {
                    var sendMessage = generateSendMessage(message.getChatId(), NEW_ALERT_START_MSG);
                    setForceReplyToMessage(sendMessage);
                    alertCache.put(message.getChatId(), Alert.builder()
                            .id(UUID.randomUUID())
                            .userId(userService.getUserByTgChatId(message.getChatId()).getId())
                            .build());
                    System.err.println(alertCache.get(message.getChatId(), Alert.class));
                    sendMessage(sendMessage);
                }
                if (messageText.equals("/rtk")) {
                    var sendMessage = generateSendMessage(message.getChatId(), "Введите время начала оповещений");
                    generateReplyKeyboard(sendMessage);
                    sendMessage(sendMessage);
                }
            } else if (message.getReplyToMessage() != null) {
                if (message.hasText()) {
                    var reply = message.getReplyToMessage();
                    if (reply.getText().equals(NEW_ALERT_CHOOSE_BUS_MSG)) {
                        alertCache.put(message.getChatId(), alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                .buses(List.of(message.getText()))
                                .build());
                        var sendMessage = generateSendMessage(message.getChatId(), NEW_ALERT_CHOOSE_DAY_MSG);
                        setForceReplyToMessage(sendMessage);
                        sendMessage(sendMessage);
                    } else if (reply.getText().equals(NEW_ALERT_CHOOSE_DAY_MSG)) {
                        alertCache.put(message.getChatId(), alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                .days(List.of(Integer.valueOf(message.getText())))
                                .build());
                        var sendMessage = generateSendMessage(message.getChatId(), NEW_ALERT_START_TIME_MSG);
                        setForceReplyToMessage(sendMessage);
                        sendMessage(sendMessage);
                    } else if (reply.getText().equals(NEW_ALERT_START_TIME_MSG)) {
                        alertCache.put(message.getChatId(), alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                .startTime(LocalTime.parse(message.getText(), DateTimeFormatter.ISO_TIME)
                                        .atOffset(ZoneOffset.of("+03:00")))
                                .build());
                        var sendMessage = generateSendMessage(message.getChatId(), NEW_ALERT_END_TIME_MSG);
                        setForceReplyToMessage(sendMessage);
                        sendMessage(sendMessage);
                    } else if (reply.getText().equals(NEW_ALERT_END_TIME_MSG)) {
                        alertCache.put(message.getChatId(), alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                .endTime(LocalTime.parse(message.getText(), DateTimeFormatter.ISO_TIME)
                                        .atOffset(ZoneOffset.of("+03:00")))
                                .build());
                        var sendMessage = generateSendMessage(message.getChatId(), "The end");
                        sendMessage(sendMessage);

                        System.err.println(alertCache.get(message.getChatId(), Alert.class));
                        alertService.createAlert(alertCache.get(message.getChatId(), Alert.class));
                    }
                }
                if (message.hasLocation()) {
                    var reply = message.getReplyToMessage();
                    if (reply.getText().equals(NEW_ALERT_START_MSG)) {
                        var location = message.getLocation();
                        alertCache.put(message.getChatId(), alertCache.get(message.getChatId(), Alert.class).toBuilder()
                                .latitude(location.getLatitude())
                                .longitude(location.getLongitude())
                                .build());
                        System.err.println(alertCache.get(message.getChatId(), Alert.class));
                        var sendMessage = generateSendMessage(message.getChatId(), NEW_ALERT_CHOOSE_BUS_MSG);
                        setForceReplyToMessage(sendMessage);
                        sendMessage(sendMessage);
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
                EditMessageText newMessage = new EditMessageText();
                newMessage.setChatId(chatId);
                newMessage.setMessageId(toIntExact(messageId));
                newMessage.setText(answer);
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void generateReplyKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        var row = new KeyboardRow();
        row.add("123");
        KeyboardButton btn = new KeyboardButton();
        btn.setText("457457");
        row.add(btn);
        rows.add(row);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return properties.getBotName();
    }

    @Override
    public void onRegister() {
        log.info("Bot successful registered");
        super.onRegister();
    }

    public CompletableFuture<Void> sendMessage(SendMessage sendMessage) {
        log.info("Sending message to {}", sendMessage.getChatId());
        try {
            return executeAsync(sendMessage)
                    .handle((message, throwable) -> {
                        if (Objects.nonNull(throwable)) {
                            log.error("Send message to {} failure with error {}",
                                    sendMessage.getChatId(), throwable.getMessage());
                        } else {
                            log.info("Message successful sent to {}", sendMessage.getChatId());
                        }
                        return null;
                    });
        } catch (TelegramApiException ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }

    private SendMessage generateSendMessage(long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .parseMode(HTML)
                .disableWebPagePreview(true)
                .text(text)
                .build();
    }

    private InlineKeyboardButton generateInlineButton(String buttonText, String buttonCallbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText(buttonText);
        button.setCallbackData(buttonCallbackData);

        return button;
    }

    private SendMessage setInlineButtonToMessage(SendMessage message, List<List<InlineKeyboardButton>> inlineButtons) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(inlineButtons);
        message.setReplyMarkup(markupInline);
        return message;
    }

    private void setForceReplyToMessage(SendMessage message) {
        ForceReplyKeyboard forceReply = new ForceReplyKeyboard();
        forceReply.setForceReply(true);
        message.setReplyMarkup(forceReply);
    }
}
