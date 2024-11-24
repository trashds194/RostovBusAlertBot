package com.rostov.transport.telegrambot.properties;

import jakarta.validation.constraints.NotNull;

public record BusApiProperties(
        @NotNull String url) {

}
