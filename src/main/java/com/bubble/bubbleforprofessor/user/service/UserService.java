package com.bubble.bubbleforprofessor.user.service;

import com.bubble.bubbleforprofessor.user.dto.JoinRequestDto;
import com.bubble.bubbleforprofessor.user.dto.UserRequestDto;
import com.bubble.bubbleforprofessor.user.dto.UserResponseDto;

public interface UserService {
    String createUser(JoinRequestDto joinRequestDto);
    UserResponseDto getUser(String id);
    void updateUser(String id, UserRequestDto userRequestDto);
    void deleteUser(String id);
}
