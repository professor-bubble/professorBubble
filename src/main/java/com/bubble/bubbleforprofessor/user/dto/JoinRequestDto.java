package com.bubble.bubbleforprofessor.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(max = 10, message = "아이디는 10자 이하여야 합니다.")
    String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(max = 10, message = "비밀번호는 10자 이하여야 합니다.")
    String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 10, message = "이름는 10자 이하여야 합니다.")
    String userName;

    @NotBlank(message = "이메일은 필수입니다.")
    String email;

    @NotBlank(message = "핸드폰 번호는 필수입니다.")
    @Size(max = 11, message = "핸드폰 번호를 '-'없이 숫자로 11자 입력해주세요")
    String phoneNumber;

    //    @NotBlank(message = "대학번호는 필수입니다.")
    int universityId;

    @NotBlank(message = "유저 유형은 필수입니다.")
    String role;

    // 교수 정보 입력
    String description;
    int professorNumber;
    String department;
}
