package com.rostov.transport.telegrambot.schedulers.impl;

import static com.rostov.transport.telegrambot.contants.MessagesConstants.TELEGRAM_BOT_MSG_BUS_ALERT;
import static com.rostov.transport.telegrambot.utils.AppUtils.format;
import static org.apache.lucene.util.SloppyMath.haversinMeters;

import com.rostov.transport.telegrambot.gateway.BusGateway;
import com.rostov.transport.telegrambot.model.Alert;
import com.rostov.transport.telegrambot.schedulers.AlertScheduler;
import com.rostov.transport.telegrambot.service.core.AlertService;
import com.rostov.transport.telegrambot.service.core.UserService;
import com.rostov.transport.telegrambot.service.telegram.TelegramBotSendMessageService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class AlertSchedulerImpl implements AlertScheduler {

    private final TelegramBotSendMessageService tgSendMessageService;
    private final AlertService alertService;
    private final UserService userService;
    private final BusGateway busGateway;

    @Override
    @Scheduled(cron = "*/20 * * * * *")
    public void getAlertsByDateAndSendMessage() {
        List<Alert> alerts = alertService.findAllByDate(OffsetDateTime.now());
        alerts.forEach(alert -> {
            var busList = busGateway.getBusListByCode(alert.getBuses());
            busList.forEach(bus -> {
                double busLat = new BigDecimal(bus.getLat()).movePointLeft(6).doubleValue();
                double busLon = new BigDecimal(bus.getLon()).movePointLeft(6).doubleValue();
                double haversineMeters = haversinMeters(alert.getLatitude(), alert.getLongitude(),
                        busLat, busLon);

                if (haversineMeters < 200) {
                    var user = userService.getUserById(alert.getUserId());
                    tgSendMessageService.sendMessage(
                            SendMessage.builder()
                                    .chatId(user.getTgChatId())
                                    .text(format(TELEGRAM_BOT_MSG_BUS_ALERT, bus.getName(),
                                            bus.getNum()))
                                    .build());
                }
            });
        });
    }
}
