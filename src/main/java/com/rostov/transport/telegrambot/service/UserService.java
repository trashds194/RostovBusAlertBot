package com.rostov.transport.telegrambot.service;

import com.rostov.transport.telegrambot.model.User;

import java.util.UUID;

public interface UserService {

    void createUser(User user);

    User getUserById(UUID id);

    User getUserByTgChatId(long tgChatId);
}
