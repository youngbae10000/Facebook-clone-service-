package com.github.prgrms.social.controller;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.error.ServiceRuntimeException;
import com.github.prgrms.social.error.UnauthorizedException;
import com.github.prgrms.social.model.api.response.ApiResult;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> notFoundException(Exception e){
        return newResponse(e, HttpStatus.NOT_FOUND);
    }

    /*HTTP 400 오류처리*/
    @ExceptionHandler({ IllegalStateException.class, IllegalArgumentException.class,
                        TypeMismatchException.class, MissingServletRequestPartException.class,
                        JSONException.class})
    public ResponseEntity<?> badRequest(Exception e){
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    /*HTTP 415 오류처리*/
    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> unsupportedMediaType(Exception e){
        return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /*HTTP 405 오류처리*/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotAllowed(Exception e){
        return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /*HTTP 500 오류처리*/
    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e){
        if(e instanceof NotFoundException)
            return newResponse(e, HttpStatus.NOT_FOUND);
        if(e instanceof UnauthorizedException)
            return newResponse(e, HttpStatus.UNAUTHORIZED);

        log.warn("Unexpected service exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiResult> internalServerError(Exception e){
        log.error("Unexpected exception occurred: {}", e.getMessage());
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}