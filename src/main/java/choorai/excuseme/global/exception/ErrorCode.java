package choorai.excuseme.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 에러 코드 추가하기
    // TODO : 테스트를 위한 예외로 실제 예외를 추가할 때 지우시면 됩니다.
    TEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 입력", "입력을 잘못했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String Message;
}
