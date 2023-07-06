package com.rostov.transport.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    private Long lon;
    private Long lat;
    private String name;
    private String type;
    private String num;
}
