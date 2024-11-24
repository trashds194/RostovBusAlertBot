package com.rostov.transport.telegrambot.common;

import static com.rostov.transport.telegrambot.contants.ApplicationConstants.DEFAULT_APP_LOCALE;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class MessageSource extends ReloadableResourceBundleMessageSource {

    public MessageSource(String baseName) {
        super.setBasename(baseName);
        super.setDefaultEncoding(StandardCharsets.UTF_8.name());
        super.setUseCodeAsDefaultMessage(true);
        super.setDefaultLocale(DEFAULT_APP_LOCALE);
    }

    public String getMessage(String code) {
        return getMessage(code, null, DEFAULT_APP_LOCALE);
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }
}
