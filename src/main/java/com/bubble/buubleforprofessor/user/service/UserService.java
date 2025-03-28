package com.bubble.buubleforprofessor.user.service;

import com.bubble.buubleforprofessor.user.dto.JoinRequestDto;

import java.util.UUID;

public interface UserService {
    String createUser(JoinRequestDto joinRequestDto);
    boolean isExist(UUID userId);
}
