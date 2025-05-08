package com.bubble.bubbleforprofessor.user.dto;

import com.bubble.bubbleforprofessor.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String loginId;
    private Timestamp createdAt;
    private Timestamp lastLoginAt;
    private String name;
    private String phoneNumber;
    private String email;
    private String universityName;
    private String roleName;

    public static UserResponseDto fromUserEntity(User userEntity) {
        return new UserResponseDto(
                userEntity.getId().toString(),
                userEntity.getLoginId(),
                userEntity.getCreatedAt(),
                userEntity.getLastLoginAt(),
                userEntity.getName(),
                userEntity.getPhoneNumber(),
                userEntity.getEmail(),
                userEntity.getUniversity().getUniversityName(),
                userEntity.getRole().getName()
        );
    }
}
