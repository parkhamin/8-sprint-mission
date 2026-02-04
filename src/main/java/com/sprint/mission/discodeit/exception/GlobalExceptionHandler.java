package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 커스텀 예외 처리
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    log.error("예외 발생 - code: {}, message: {}", ex.getErrorCode(),
        ex.getMessage());

    HttpStatus status = ex.getErrorCode().getStatus();

    ErrorResponse errorResponse = new ErrorResponse(
        ex.getTimestamp(),
        ex.getErrorCode().name(),
        ex.getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        status.value()
    );

    return ResponseEntity.status(status).body(errorResponse);
  }

  // Fallback 핸들러
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("예상치 못 한 예외 발생", ex);

    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    HttpStatus status = errorCode.getStatus();

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        errorCode.name(),
        errorCode.getMessage(),
        null,
        ex.getClass().getSimpleName(),
        status.value()
    );

    return ResponseEntity.status(status).body(errorResponse);
  }
}
