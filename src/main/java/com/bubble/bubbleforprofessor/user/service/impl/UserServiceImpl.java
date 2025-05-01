package com.bubble.bubbleforprofessor.user.service.impl;

import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.dto.JoinProfessorRequestDto;
import com.bubble.bubbleforprofessor.user.dto.JoinRequestDto;
import com.bubble.bubbleforprofessor.user.entity.Professor;
import com.bubble.bubbleforprofessor.user.entity.Role;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.RoleRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import com.bubble.bubbleforprofessor.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final ProfessorRepository professorRepository;

    @Override
    public String createUser(JoinRequestDto joinRequestDto) {
        // 아이디 검증
        if (userRepository.existsByLoginId(joinRequestDto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_USERNAME);
        } else if (joinRequestDto.getLoginId() == null || joinRequestDto.getLoginId().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_USERID);
        }

        // 이메일 검증
        if (userRepository.existsByEmail(joinRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);
        }


        // Role 확인
        // TODO: 회원가입 방법 uri로 확인?
        Role roleUser = roleRepository.findByName("STUDENT").orElseThrow();

        User user = User.builder()
                .loginId(joinRequestDto.getLoginId())
                .name(joinRequestDto.getUserName())
                .password(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()))
                .email(joinRequestDto.getEmail())
                .phoneNumber(joinRequestDto.getPhoneNumber())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .role(roleUser)
                .university(null) // Todo University 주입
                .build();

        try {
            userRepository.save(user);
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            log.debug("insert fail - 무결서 위반", e);
            return "insert fail";
        }

        return "join Success";
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public String createUser1(JoinRequestDto joinRequestDto, JoinProfessorRequestDto joinProfessorRequestDto) {
        // 아이디 검증
        if (userRepository.existsByLoginId(joinRequestDto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_USERNAME);
        } else if (joinRequestDto.getLoginId() == null || joinRequestDto.getLoginId().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_USERID);
        }
        // 이메일 검증
        if (userRepository.existsByEmail(joinRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_EMAIL);
        }
        // role 확인
        Role role = roleRepository.findByName(joinRequestDto.getRole().toUpperCase()).orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_ROLE));

        User user = User.builder()
                .loginId(joinRequestDto.getLoginId())
                .name(joinRequestDto.getUserName())
                .password(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()))
                .email(joinRequestDto.getEmail())
                .phoneNumber(joinRequestDto.getPhoneNumber())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .role(role)
                .university(null) // Todo University 주입
                .build();

        try {
            userRepository.save(user);
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            log.debug("insert fail - 무결서 위반", e);
            return "insert fail";
        }

        if (role.getName().equals("PROFESSOR")) {
            Professor professor = Professor.builder()
                    .user(user)
                    .professorNum(joinProfessorRequestDto.getProfessorNumber())
                    .department(joinProfessorRequestDto.getDepartment())
                    .description(joinProfessorRequestDto.getDescrption())
                    .isApproved(false)
                    .build();

            professorRepository.save(professor);
        }

        return "new join Success";
    }
}
