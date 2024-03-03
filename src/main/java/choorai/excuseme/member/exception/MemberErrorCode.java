package choorai.excuseme.member.exception;

import choorai.excuseme.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {

    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "잘못된 입력", "이미 존재하는 회원입니다."),
    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "잘못된 입력", "해당 유저를 찾을 수 없습니다."),
    WRONG_USERNAME(HttpStatus.BAD_REQUEST, "잘못된 입력", "이메일 형식이 아닙니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 입력", "비밀번호는 숫자, 영어, 특수문자를 모두 포함한 6자에서 20자이어야 합니다."),
    WRONG_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "잘못된 입력", "전화번호는 -를 제외한 11자이어야 합니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    MemberErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
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
