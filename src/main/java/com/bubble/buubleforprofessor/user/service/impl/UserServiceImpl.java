package com.bubble.buubleforprofessor.user.service.impl;

import com.bubble.buubleforprofessor.user.dto.JoinRequestDto;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import com.bubble.buubleforprofessor.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String createUser(JoinRequestDto joinRequestDto) {
        // 아이디 확인
        boolean isExist = userRepository.existsByLoginId(joinRequestDto.getLoginId());
        if (isExist) {
            return "join Fail - Exist id";
        }

        User user = User.builder()
                .loginId(joinRequestDto.getLoginId())
                .name(joinRequestDto.getUserName())
                .password(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()))
                .build();

        userRepository.save(user);

        return "join Success";
    }


}
