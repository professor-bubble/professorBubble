package com.bubble.bubbleforprofessor.user.service;

import com.bubble.bubbleforprofessor.user.dto.JoinRequestDto;

public interface UserService {
    String createUser(JoinRequestDto joinRequestDto);
}
