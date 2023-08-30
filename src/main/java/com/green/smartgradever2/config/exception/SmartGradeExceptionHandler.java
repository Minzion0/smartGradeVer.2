package com.green.smartgradever2.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Slf4j
@RestControllerAdvice
public class SmartGradeExceptionHandler extends ResponseEntityExceptionHandler {

    //401ExceptionHandler
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleClientErrorException(HttpServletRequest request) {
        return handleExceptionInternal(CommonErrorCode.AUTHENTICATION_ERROR, CommonErrorCode.AUTHENTICATION_ERROR.getMessage(), request.getRequestURI());
    }

    //AdminException생성하여 Admin관련 ExceptionHandling
    @ExceptionHandler(AdminException.class)
    public ResponseEntity<Object> handleAdminException(AdminException e, HttpServletRequest request) {
        return handleAdminExceptionSet(CommonErrorCode.ADMIN_EXCEPTION, e.getMsg(), request.getRequestURI());
    }

    //설정한것 이외의 모든 에러 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex, HttpServletRequest request) {
        if (ex.getMessage()!=null) {
            if (ex.getMessage().length() < 30) {
                return handleExceptionInternal(CommonErrorCode.GLOBAL_EXCEPTION, ex.getMessage(), request.getRequestURI());
            }
        }
        return handleExceptionInternal(CommonErrorCode.GLOBAL_EXCEPTION, CommonErrorCode.GLOBAL_EXCEPTION.getMessage(), request.getRequestURI());
    }


    private ResponseEntity<Object> handleAdminExceptionSet(ErrorCode errorCode, String msg, String path) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode, msg, path));
    }


    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message, String path) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message, path));
    }

    // 에러 메시지 정리
    private MyErrorResponse makeErrorResponse(ErrorCode errorCode, String message, String path) {
        return MyErrorResponse.builder()
                .code(errorCode.getMessage())
                .message(message)
                .path(path)
                .dateTime(now())
                .build();
    }


    //에러 발생시간
    private String now() {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd // HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }
}