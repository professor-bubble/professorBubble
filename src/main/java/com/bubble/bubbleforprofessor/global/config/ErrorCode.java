package com.bubble.bubbleforprofessor.global.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // msg, Http code(200)
    /**
     * USER
     */
    // 아이디
    DUPLICATE_USER_USERNAME("이미 사용 중인 아이디입니다", HttpStatus.CONFLICT),
    INVALID_USERID("적절하지 않은 사용자 아이디입니다.",HttpStatus.BAD_REQUEST),

    // 이미 사용중인 이메일
    DUPLICATE_USER_EMAIL("이미 사용 중인 이메일입니다", HttpStatus.CONFLICT),


    // 존재하지않는 유저
    NON_EXISTENT_USER("존재하지 않는 유저입니다", HttpStatus.BAD_REQUEST),

    // 회원가입에 필요한 값 필요
    INVALID_USER_DATA("회원가입에 필요한 값이 누락되었습니다", HttpStatus.BAD_REQUEST),

    // 사용자 일치 불일치
    USER_MISMATCH("사용자가 일치하지 않습니다", HttpStatus.FORBIDDEN),

    //권한 없음
    USER_UNAUTHORIZED("사용자가 권한이없음",HttpStatus.UNAUTHORIZED),

    // 존재하지않는 교수
    NON_EXISTENT_PROFESSOR("존재하지 않는 교수입니다", HttpStatus.NOT_FOUND),

    // 이미 존재하는 교수
    EXISTENT_PROFESSOR("이미 존재하는 교수입니다", HttpStatus.CONFLICT),

    /**
     * Role
     */
    // 존재하지않는 권한입니다.
    NON_EXISTENT_ROLE("존재하지 않는 권한입니다.", HttpStatus.NOT_FOUND),

    /**
     * JWT
     */
    EXPIRED_JWT("JWT의 유효기간이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

    /**
     * UNIVERSITY
     */
    UNIVERSITY_API_CALL_FAILED("대학교 외부 API 호출에 실패했습니다", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("요청이 유효하지 않습니다", HttpStatus.BAD_REQUEST),
    UNI_API_RESPONSE_NULL("대학교 외부API 응답이 없습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    BODY_MISSING("대학교 이름이 응답 본문이 없습니다", HttpStatus.BAD_GATEWAY),
    UNIVERSITYNAME_INVALID_REQUEST("검색어(universityName)는 필수입니다", HttpStatus.BAD_REQUEST),
    UNIVERSITY_NOT_FOUND("해당 검색어에 맞는 대학교를 찾을 수 없습니다",HttpStatus.NOT_FOUND),
    /**
    * skin
     */
    // 존재하지않는 스킨입니다.
    NON_EXISTENT_SKIN("존재하지 않는 스킨입니다.", HttpStatus.NOT_FOUND),
    // 가지고있지 않은 스킨 입니다.
    NON_EXISTENT_USER_SKIN("가지고있지 않은 스킨입니다.", HttpStatus.NOT_FOUND),
    /**
     * payment
     */

    /**
     * chat
     */
    // 이미 존재하는 채팅방입니다.
    EXISTENT_CHATROOM("이미 존재하는 채팅방입니다.", HttpStatus.CONFLICT),

    // 존재하지않는 채팅방입니다.
    NON_EXISTENT_CHATROOM("존재하지 않는 채팅방입니다.", HttpStatus.NOT_FOUND),

    //내 채팅방이 아닌경우
    NON_EXISTENT_CHATROOM_USER("내가 존재하는 채팅방이 아닙니다.",HttpStatus.NOT_FOUND),

    //내가 이미 존재하는 채팅방인경우
    EXISTENT_CHATROOM_USER("내가 이미 존재하는 채팅방입니다.",HttpStatus.CONFLICT);

    private final String msg;
    private final HttpStatus status;

    ErrorCode(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }
}
