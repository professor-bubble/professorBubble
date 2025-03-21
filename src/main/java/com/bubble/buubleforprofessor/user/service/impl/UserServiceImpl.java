package com.bubble.buubleforprofessor.user.service.impl;


import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import com.bubble.buubleforprofessor.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public boolean isExist(UUID userId) {

        return userRepository.existsById(userId);
    }
}
