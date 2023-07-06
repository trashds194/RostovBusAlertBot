package com.rostov.transport.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private UUID id;
    private UUID userId;
    private Double latitude;
    private Double longitude;
    private List<Integer> days;
    private List<String> buses;
    private OffsetTime startTime;
    private OffsetTime endTime;
    private OffsetDateTime createdDate;
}
