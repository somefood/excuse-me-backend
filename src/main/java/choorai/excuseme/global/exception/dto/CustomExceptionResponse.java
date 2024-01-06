package choorai.excuseme.global.exception.dto;

import choorai.excuseme.global.exception.ErrorCode;
import org.springframework.http.ResponseEntity;

public record CustomExceptionResponse(String code, String errorMessage) {

    public static ResponseEntity<CustomExceptionResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus().value())
                .body(new CustomExceptionResponse(errorCode.getCode(), errorCode.getMessage()));
    }
}
