package com.github.prgrms.social.controller;

import com.github.prgrms.social.model.api.response.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static com.github.prgrms.social.model.api.response.ApiResult.ERROR;

@ControllerAdvice
public class GeneralExceptionHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ResponseEntity<ApiResult> newResponse(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(throwable, status), headers, status);
    }

    // TODO REST API 처리 중 발생한 예외를 catch 하고, 로그를 남기고, ApiResult를 사용해 오류 응답을 전달

}