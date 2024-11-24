package com.rostov.transport.telegrambot.contants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rostov.transport.telegrambot.model.Bus;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationConstants {

    // application constants
    public static final Locale DEFAULT_APP_LOCALE = Locale.of("ru_RU");
    public static final ZoneOffset DEFAULT_APP_ZONE_OFFSET = ZoneOffset.of("+03:00");

    // typeReferences constants
    public static final TypeReference<Map<String, List<Integer>>> MAP_TYPE_REF = new TypeReference<>() {
    };
    public static final TypeReference<List<Bus>> LIST_BUS_TYPE_REF = new TypeReference<>() {
    };
}
