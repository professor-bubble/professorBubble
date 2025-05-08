package com.bubble.bubbleforprofessor.user.service.impl;

import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.dto.JoinRequestDto;
import com.bubble.bubbleforprofessor.user.dto.UserRequestDto;
import com.bubble.bubbleforprofessor.user.dto.UserResponseDto;
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
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final ProfessorRepository professorRepository;

    @Transactional(rollbackOn = Exception.class)
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
            if (joinRequestDto.getProfessorNumber() == 0) {
                throw new CustomException(ErrorCode.INVALID_PROFESSOR_NUMBER);
            }

            Professor professor = Professor.builder()
                    .user(user)
                    .professorNum(joinRequestDto.getProfessorNumber())
                    .department(joinRequestDto.getDepartment())
                    .description(joinRequestDto.getDescription())
                    .isApproved(false)
                    .build();

            professorRepository.save(professor);
        }

        return "new join Success";
    }

    @Override
    public UserResponseDto getUser(String id) {
        UUID uuid = UUID.fromString(id);

        User userEntity = userRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        return UserResponseDto.fromUserEntity(userEntity);
    }

    @Override
    public void updateUser(String id, UserRequestDto userRequestDto) {
        if (!id.equals(userRequestDto.getId())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        UUID uuid = UUID.fromString(userRequestDto.getId());

        User userEntity = userRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        userEntity.userFromDto(userRequestDto);
    }

    @Override
    public void deleteUser(String id) {
        UUID uuid = UUID.fromString(id);

        userRepository.deleteById(uuid);
    }
}
