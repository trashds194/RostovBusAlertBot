package com.rostov.transport.telegrambot.repository;

import com.rostov.transport.telegrambot.model.User;
import java.util.UUID;

public interface UserRepository {

    void insert(User user);

    // TODO Optional
    User findUserById(UUID id);

    boolean existsByTgChatId(long tgChatId);

    User findUserByTgChatId(long tgChatId);
}
