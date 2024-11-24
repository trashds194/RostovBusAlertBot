package com.rostov.transport.telegrambot.service.core;

import com.rostov.transport.telegrambot.model.User;
import java.util.UUID;

public interface UserService {

    void createUser(User user);

    User getUserById(UUID id);

    boolean existsByTgChatId(long tgChatId);

    User getUserByTgChatId(long tgChatId);
}
