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

    /**
     * UNIVERSITY
     */
    UNIVERSITY_API_CALL_FAILED("대학교 외부 API 호출에 실패했습니다", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("요청이 유효하지 않습니다", HttpStatus.BAD_REQUEST);

    /**
    * skin
     */

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
