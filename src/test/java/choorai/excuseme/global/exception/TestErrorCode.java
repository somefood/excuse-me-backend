package choorai.excuseme.global.exception;

import org.springframework.http.HttpStatus;

public enum TestErrorCode implements ErrorCode{

    TEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 정보 입력", "해당 입력은 잘못되었습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    TestErrorCode(final HttpStatus httpStatus, final String errorCode, final String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
