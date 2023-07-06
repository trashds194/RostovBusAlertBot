package com.rostov.transport.telegrambot.repository.impl;

import com.rostov.transport.telegrambot.model.User;
import com.rostov.transport.telegrambot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.jooq.generated.Tables.T_USER_INFO;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dslContext;

    @Override
    public void insert(User user) {
        dslContext.insertInto(T_USER_INFO)
                .set(T_USER_INFO.ID, UUID.randomUUID())
                .set(T_USER_INFO.TG_CHAT_ID, user.getTgChatId())
                .set(T_USER_INFO.USER_NAME, user.getUserName())
                .set(T_USER_INFO.USER_NICK, user.getUserNick())
                .set(T_USER_INFO.CREATED_DATE, OffsetDateTime.now(ZoneOffset.of("+03:00")))
                .execute();
    }

    @Override
    public User findUserById(UUID id) {
        return dslContext.selectFrom(T_USER_INFO)
                .where(T_USER_INFO.ID.eq(id))
                .fetchOneInto(User.class);
    }

    @Override
    public User findUserByTgChatId(long tgChatId) {
        return dslContext.selectFrom(T_USER_INFO)
                .where(T_USER_INFO.TG_CHAT_ID.eq(tgChatId))
                .fetchOneInto(User.class);
    }
}
