package com.bubble.bubbleforprofessor.user.service;

import com.bubble.bubbleforprofessor.user.dto.JoinProfessorRequestDto;
import com.bubble.bubbleforprofessor.user.dto.JoinRequestDto;

public interface UserService {
    String createUser(JoinRequestDto joinRequestDto);
    String createUser1(JoinRequestDto joinRequestDto, JoinProfessorRequestDto joinProfessorRequestDto);
}
