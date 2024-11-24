package com.rostov.transport.telegrambot.service.core.impl;

import com.rostov.transport.telegrambot.model.Alert;
import com.rostov.transport.telegrambot.repository.AlertRepository;
import com.rostov.transport.telegrambot.service.core.AlertService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    // TODO кнопка просмотра алертов
    // TODO кнопка остановки алертов
    // TODO кнопка остановки всех алертов
    // TODO кнопка удаления алертов

    @Override
    public List<Alert> findAllByDate(OffsetDateTime date) {
        return alertRepository.findAllByDate(date);
    }

    @Override
    public void createAlert(Alert alert) {
        alertRepository.insert(alert);
    }
}
