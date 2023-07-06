package com.rostov.transport.telegrambot.repository.impl;

import com.rostov.transport.telegrambot.model.Alert;
import com.rostov.transport.telegrambot.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.jooq.generated.Tables.T_USER_ALERT;
import static org.jooq.impl.DSL.val;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryImpl implements AlertRepository {

    private final DSLContext dslContext;

    @Override
    public void insert(Alert alert) {
        dslContext.insertInto(T_USER_ALERT)
                .set(T_USER_ALERT.ID, alert.getId())
                .set(T_USER_ALERT.USER_ID, alert.getUserId())
                .set(T_USER_ALERT.LATITUDE, alert.getLatitude())
                .set(T_USER_ALERT.LONGITUDE, alert.getLongitude())
                .set(T_USER_ALERT.DAYS, alert.getDays().toArray(new Integer[0]))
                .set(T_USER_ALERT.BUSES, alert.getBuses().toArray(new String[0]))
                .set(T_USER_ALERT.START_DATE, alert.getStartTime().withOffsetSameLocal(ZoneOffset.of("+03:00")))
                .set(T_USER_ALERT.END_DATE, alert.getEndTime().withOffsetSameLocal(ZoneOffset.of("+03:00")))
                .set(T_USER_ALERT.CREATED_DATE, OffsetDateTime.now(ZoneOffset.of("+03:00")))
                .execute();
    }

    @Override
    public List<Alert> findAllByUserId(UUID userId) {
        return dslContext.selectFrom(T_USER_ALERT)
                .where(T_USER_ALERT.USER_ID.eq(userId))
                .fetchInto(Alert.class);
    }

    @Override
    public List<Alert> findAllByDate(OffsetDateTime date) {
        return dslContext.selectFrom(T_USER_ALERT)
                .where(T_USER_ALERT.START_DATE.le(date.toOffsetTime()))
                .and(T_USER_ALERT.END_DATE.ge(date.toOffsetTime()))
                .and(val(date.getDayOfWeek().getValue()).eq(DSL.any(T_USER_ALERT.DAYS)))
                .fetchInto(Alert.class);
    }
}
