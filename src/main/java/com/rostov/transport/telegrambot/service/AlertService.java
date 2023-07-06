package com.rostov.transport.telegrambot.service;

import com.rostov.transport.telegrambot.model.Alert;

public interface AlertService {
    void getAlertsByDateAndSendMessage();

    void createAlert(Alert alert);
}
