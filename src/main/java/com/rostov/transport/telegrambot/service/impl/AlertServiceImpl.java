package com.rostov.transport.telegrambot.service.impl;

import com.rostov.transport.telegrambot.bot.BotComponent;
import com.rostov.transport.telegrambot.gateway.BusGateway;
import com.rostov.transport.telegrambot.model.Alert;
import com.rostov.transport.telegrambot.repository.AlertRepository;
import com.rostov.transport.telegrambot.service.AlertService;
import com.rostov.transport.telegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static com.rostov.transport.telegrambot.contants.BotMessageConstants.BUS_ALERT_MSG;
import static org.apache.lucene.util.SloppyMath.haversinMeters;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final UserService userService;
    private final BusGateway busGateway;
    private final BotComponent botComponent;

    // TODO кнопка просмотра алертов
    // TODO кнопка остановки алертов
    // TODO кнопка остановки всех алертов
    // TODO кнопка удаления алертов

    @Override
    @Scheduled(cron = "*/20 * * * * *")
    public void getAlertsByDateAndSendMessage() {
        List<Alert> alerts = alertRepository.findAllByDate(OffsetDateTime.now());
        alerts.forEach(alert -> {
            var busList = busGateway.getBusListByCode(alert.getBuses());
            busList.forEach(bus -> {
                double busLat = new BigDecimal(bus.getLat()).movePointLeft(6).doubleValue();
                double busLon = new BigDecimal(bus.getLon()).movePointLeft(6).doubleValue();
                double haversineMeters =
                    haversinMeters(alert.getLatitude(), alert.getLongitude(), busLat, busLon);
                if (haversineMeters < 200) {
                    var user = userService.getUserById(alert.getUserId());
                    botComponent.sendMessage(
                        SendMessage.builder()
                            .chatId(user.getTgChatId())
                            .text(String.format(BUS_ALERT_MSG, bus.getName(), bus.getNum()))
                            .build());
                }
            });
        });
    }

    @Override
    public void createAlert(Alert alert) {
        alertRepository.insert(alert);
    }
}
