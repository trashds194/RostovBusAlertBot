package com.rostov.transport.telegrambot.service.core.impl;

import com.rostov.transport.telegrambot.model.User;
import com.rostov.transport.telegrambot.repository.UserRepository;
import com.rostov.transport.telegrambot.service.core.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.insert(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findUserById(id);
    }

    @Override
    public boolean existsByTgChatId(long tgChatId) {
        return userRepository.existsByTgChatId(tgChatId);
    }

    @Override
    public User getUserByTgChatId(long tgChatId) {
        return userRepository.findUserByTgChatId(tgChatId);
    }
}
