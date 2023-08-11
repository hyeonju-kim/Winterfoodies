package com.winterfoodies.winterfoodies_project.exception;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.social.OAuthException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    // 회원가입 익셉션, 마이페이지 비번 변경 익셉션, 장바구니 상품추가 익셉션
    @ExceptionHandler(RequestException.class)
    public ErrorBox requestException(RequestException requestException) {
        return requestException.getErrorBox();
    }

    // 로그인 익셉션
    @ExceptionHandler(BadCredentialsException.class)
    public String requestException(BadCredentialsException badCredentialsException) {
        return badCredentialsException.getMessage();
    }

    // 소셜로그인 익셉션
    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<String> oAuthExceptionHandler(OAuthException e) {
        log.info("e : {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 소셜로그인 타입변환 익셉션
    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<String> conversionExceptionHandler() {
        return new ResponseEntity<>("알 수 없는 SocialLoginType 입니다.", HttpStatus.NOT_FOUND);
    }



 






//    // 특정한 사이트 차단
//    @ExceptionHandler(IllegalUrlException.class)
//    public String illeUrl(IllegalUrlException e, Model model) {
//        String message = e.getMessage();
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//
//        model.addAttribute("message", message);
//        model.addAttribute("status", status.value());
//
//        return "error";
//    }
//
//    // 중복된 url 처리
//    @ExceptionHandler(DuplicatedException.class)
//    public String dupliEx(DuplicatedException e, Model model) {
//        String message = e.getMessage();
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//
//        model.addAttribute("message", message);
//        model.addAttribute("status", status.value());
//
//        return "error";
//    }

    //ResponseDto
    @Data
    @AllArgsConstructor
    static class ResponseDto{
        private String code;
        private String msg;
    }
}
