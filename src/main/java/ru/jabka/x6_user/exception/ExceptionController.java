package ru.jabka.x6_user.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.jabka.x6_user.model.ApiError;

@RestControllerAdvice
@Log4j2
public class ExceptionController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ApiError(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(new ApiError(e.getMessage()));
    }
}