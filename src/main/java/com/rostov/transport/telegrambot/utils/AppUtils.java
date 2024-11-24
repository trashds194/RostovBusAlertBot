package com.rostov.transport.telegrambot.utils;

import static com.rostov.transport.telegrambot.contants.ApplicationConstants.DEFAULT_APP_ZONE_OFFSET;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.helpers.MessageFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppUtils {

    public static String format(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, ArrayUtils.toArray(args)).getMessage();
    }

    public static OffsetTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ISO_TIME)
                .atOffset(DEFAULT_APP_ZONE_OFFSET);
    }
}
