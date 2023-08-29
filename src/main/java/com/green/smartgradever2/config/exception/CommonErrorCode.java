package com.green.smartgradever2.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    ADMIN_EXCEPTION(HttpStatus.BAD_REQUEST, "에러 발생 : 개발자 이민용을 찾아주세요"),
    GLOBAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "에러 발생 : 관리자를 찾아주세요"),
    ADMIN_PROFESSOR_ERROR(HttpStatus.NOT_EXTENDED, "계정등록 오류"),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "토큰 만료");

    private final HttpStatus httpStatus;
    private final String message;

}
