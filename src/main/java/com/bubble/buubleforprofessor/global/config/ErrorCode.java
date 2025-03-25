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
    INVALID_REQUEST("요청이 유효하지 않습니다", HttpStatus.BAD_REQUEST),
    UNI_API_RESPONSE_NULL("대학교 외부API 응답이 없습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    BODY_MISSING("대학교 이름이 응답 본문이 없습니다", HttpStatus.BAD_GATEWAY),
    UNIVERSITYNAME_INVALID_REQUEST("검색어(universityName)는 필수입니다", HttpStatus.BAD_REQUEST),
    UNIVERSITY_NOT_FOUND("해당 검색어에 맞는 대학교를 찾을 수 없습니다",HttpStatus.NOT_FOUND);

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
