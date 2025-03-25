package com.bubble.buubleforprofessor.global.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // msg, Http code(200)
    /**
     * USER
     */
    // 이미 존재하는 유저
    EXISTENT_USER("이미 존재하는 유저입니다",HttpStatus.CONFLICT),

    // 이미 사용중인 이메일
    DUPLICATE_USER_EMAIL("이미 사용 중인 이메일입니다", HttpStatus.CONFLICT),

    // 이미 사용중인 아이디
    DUPLICATE_USER_USERNAME("이미 사용 중인 아이디입니다", HttpStatus.CONFLICT),

    // 존재하지않는 유저
    NON_EXISTENT_USER("존재하지 않는 유저입니다", HttpStatus.BAD_REQUEST),

    // 회원가입에 필요한 값 필요
    INVALID_USER_DATA("회원가입에 필요한 값이 누락되었습니다", HttpStatus.BAD_REQUEST),

    // 사용자 일치 불일치
    USER_MISMATCH("사용자가 일치하지 않습니다", HttpStatus.FORBIDDEN),

    //권한 없음
    USER_UNAUTHORIZED("사용자가 권한이없음",HttpStatus.UNAUTHORIZED),

    // 적절하지 않은 사용자 아이디
    INVALID_USERID("적절하지 않은 사용자 아이디입니다.",HttpStatus.BAD_REQUEST),

    // 존재하지않는 교수
    NON_EXISTENT_PROFESSOR("존재하지 않는 교수입니다", HttpStatus.NOT_FOUND),

    // 이미 존재하는 교수
    EXISTENT_PROFESSOR("이미 존재하는 교수입니다", HttpStatus.CONFLICT),

    // 존재하지않는 권한입니다.
    NON_EXISTENT_ROLE("존재하지 않는 권한입니다.", HttpStatus.NOT_FOUND),

    /**
     * UNIVERSITY
     */
    UNIVERSITY_API_CALL_FAILED("대학교 외부 API 호출에 실패했습니다", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("요청이 유효하지 않습니다", HttpStatus.BAD_REQUEST),

    /**
    * skin
     */
    // 존재하지않는 스킨입니다.
    NON_EXISTENT_SKIN("존재하지 않는 스킨입니다.", HttpStatus.NOT_FOUND),
    // 가지고있지 않은 스킨 입니다.
    NON_EXISTENT_USER_SKIN("가지고있지 않은 스킨입니다.", HttpStatus.NOT_FOUND);
    /**
     * payment
     */

    /**
     * chat
     */


    private final String msg;
    private final HttpStatus status;

    ErrorCode(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }
}
