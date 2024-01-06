package choorai.excuseme.global.exception;

import choorai.excuseme.global.exception.dto.CustomExceptionResponse;
import choorai.excuseme.support.AcceptanceTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest extends AcceptanceTest {

    @DisplayName("예외 발생시 예외에 알맞은 응답 status와 response응답 값을 보낸다.")
    @Test
    void test_exception_result() {
        // given
        // when
        final CustomExceptionResponse result = RestAssured
                .when().get("/errorTest")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body().as(CustomExceptionResponse.class);

        // then
        final CustomExceptionResponse expect = new CustomExceptionResponse(TestErrorCode.TEST_ERROR.getCode(), TestErrorCode.TEST_ERROR.getMessage());
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(expect);
    }
}
