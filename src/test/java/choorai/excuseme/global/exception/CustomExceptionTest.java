package choorai.excuseme.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CustomExceptionTest {

    @DisplayName("입력된 정보를 Property: {인자}, Value: {입력한 값} 형태의 문자열로 반환한다.")
    @Test
    void get_property_and_value_with_delimiter() {
        // given
        final CustomException exception = new CustomException(ErrorCode.TEST_ERROR, Map.of("정보1", "테스트1"));

        // when
        final String result = exception.getInputValue();

        // then
        final String expect = "Property: 정보1, Value: 테스트1 ";
        assertThat(result).isEqualTo(expect);
    }
}
