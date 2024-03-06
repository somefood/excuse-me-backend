package choorai.excuseme.oauth.exception;

import choorai.excuseme.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OauthErrorCode implements ErrorCode {

    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "잘못된 입력", "해당 유저를 찾을 수 없습니다."),
    FAIL_GOOGLE_OAUTH(HttpStatus.BAD_REQUEST, "처리 과정 에러", "Google 사용자 정보를 가져오는데 실패했습니다."),
    INVALID_SOCIAL_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 입력", "알 수 없는 소셜 로그인 형식입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    OauthErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
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
