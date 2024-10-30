package com.sparta.scheduledevelop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode {
    // user error
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    USER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 권한을 찾을 수 없습니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일 입니다."),
    USER_PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),

    // schedule error
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),

    // comment error
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // token error
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    TOKEN_UNSIGNED(HttpStatus.BAD_REQUEST, "유효하지 않는 JWT 서명입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),

    // auth error
    ADMIN_PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "관리자 암호가 틀렸습니다."),
    INVALID_ADD_AUTHOR(HttpStatus.UNAUTHORIZED, "담당 유저를 배치할 권한이 없습니다."),
    INVALID_UPDATE(HttpStatus.UNAUTHORIZED, "수정할 권한이 없습니다."),
    INVALID_DELETE(HttpStatus.UNAUTHORIZED, "삭제할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
