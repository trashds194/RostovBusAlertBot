package com.rostov.transport.telegrambot.service.core;

import com.rostov.transport.telegrambot.model.Alert;
import java.time.OffsetDateTime;
import java.util.List;

public interface AlertService {

    List<Alert> findAllByDate(OffsetDateTime date);

    void createAlert(Alert alert);
}
