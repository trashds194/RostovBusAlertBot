package com.rostov.transport.telegrambot.service.impl;

import com.rostov.transport.telegrambot.model.User;
import com.rostov.transport.telegrambot.repository.UserRepository;
import com.rostov.transport.telegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    public User getUserByTgChatId(long tgChatId) {
        return userRepository.findUserByTgChatId(tgChatId);
    }
}
