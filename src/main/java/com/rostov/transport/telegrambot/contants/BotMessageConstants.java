package com.rostov.transport.telegrambot.contants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BotMessageConstants {
    public static final String HELLO_MSG = "Здравствуйте %s! Воспользуйтесь командами из меню для создания напоминания и следуйте инструкции!";
    public static final String NEW_ALERT_START_MSG = """
        Следуйте дальнейшей инструкции, чтобы создать новое уведомление!
        1. Отправьте точку, на которую сработает уведомление.
        Для отправки точки используйте вложения -> Геопозиция -> выберите точку -> Отправить геопозицию""";

    public static final String NEW_ALERT_CHOOSE_BUS_MSG = "2. Введите номер автобуса, на который должно сработать уведомление";
    public static final String NEW_ALERT_CHOOSE_DAY_MSG = "3. Введите номер дня недели, на который должно сработать уведомление";
    public static final String NEW_ALERT_START_TIME_MSG = "4. Введите время начала срабатывания уведомления";
    public static final String NEW_ALERT_END_TIME_MSG = "4. Введите время окончания срабатывания уведомления";

    public static final String BUS_ALERT_MSG = "Атобус %s с номером %s подъезжает!";
}
