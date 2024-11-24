package com.rostov.transport.telegrambot.repository.impl;

import static com.rostov.transport.telegrambot.contants.ApplicationConstants.DEFAULT_APP_ZONE_OFFSET;
import static org.jooq.generated.Tables.T_USER_INFO;

import com.rostov.transport.telegrambot.model.User;
import com.rostov.transport.telegrambot.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

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
                .set(T_USER_INFO.CREATED_DATE, OffsetDateTime.now(DEFAULT_APP_ZONE_OFFSET))
                .execute();
    }

    @Override
    public User findUserById(UUID id) {
        return dslContext.selectFrom(T_USER_INFO)
                .where(T_USER_INFO.ID.eq(id))
                .fetchOneInto(User.class);
    }

    @Override
    public boolean existsByTgChatId(long tgChatId) {
        return dslContext.fetchExists(dslContext.selectFrom(T_USER_INFO)
                .where(T_USER_INFO.TG_CHAT_ID.eq(tgChatId)));
    }

    @Override
    public User findUserByTgChatId(long tgChatId) {
        return dslContext.selectFrom(T_USER_INFO)
                .where(T_USER_INFO.TG_CHAT_ID.eq(tgChatId))
                .fetchOneInto(User.class);
    }
}
