package com.rostov.transport.telegrambot.contants;

import com.rostov.transport.telegrambot.common.MessageSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessagesConstants {

    public static final MessageSource SYSTEM_MESSAGES = new MessageSource(
            "classpath:messages/system-messages");
    public static final MessageSource BUSINESS_MESSAGES = new MessageSource(
            "classpath:messages/business-messages");

    /// System messages
    // info messages
    public static final String SENDING_MESSAGE = SYSTEM_MESSAGES.getMessage(
            "telegram.bot.sending.message");

    // debug messages
    public static final String SENDING_MESSAGE_SUCCESSFUL = SYSTEM_MESSAGES.getMessage(
            "telegram.bot.sending.message.successful");
    public static final String TELEGRAM_BOT_EXECUTED_DATA = SYSTEM_MESSAGES.getMessage(
            "telegram.bot.executed.data");
    public static final String TELEGRAM_BOT_RECEIVE_MESSAGE = SYSTEM_MESSAGES.getMessage(
            "telegram.bot.receive.message");
    public static final String TELEGRAM_BOT_ALERT_DATA = SYSTEM_MESSAGES.getMessage(
            "telegram.bot.alert.data");

    // error messages
    public static final String SENDING_MESSAGE_ERROR = SYSTEM_MESSAGES.getMessage(
            "telegram.bot.sending.message.error");

    /// Business messages
    // command messages
    public static final String TELEGRAM_BOT_COMMAND_START = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.command.start");
    public static final String TELEGRAM_BOT_COMMAND_NEW_ALERT = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.command.new_alert");

    // bot messages
    public static final String TELEGRAM_BOT_MSG_HELLO = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.hello");
    public static final String TELEGRAM_BOT_MSG_NEW_ALERT_START = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.new_alert.start");
    public static final String TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_BUS = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.new_alert.choose.bus");
    public static final String TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_DAY = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.new_alert.choose.day");
    public static final String TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_START_TIME = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.new_alert.choose.start_time");
    public static final String TELEGRAM_BOT_MSG_NEW_ALERT_CHOOSE_END_TIME = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.new_alert.choose.end_time");
    public static final String TELEGRAM_BOT_MSG_BUS_ALERT = BUSINESS_MESSAGES.getMessage(
            "telegram.bot.message.bus.alert");
}
