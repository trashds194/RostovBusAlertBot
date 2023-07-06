package com.rostov.transport.telegrambot.repository;

import com.rostov.transport.telegrambot.model.Alert;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AlertRepository {

    void insert(Alert alert);

    List<Alert> findAllByUserId(UUID userId);

    List<Alert> findAllByDate(OffsetDateTime date);
}
