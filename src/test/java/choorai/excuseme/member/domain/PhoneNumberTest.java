package choorai.excuseme.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import choorai.excuseme.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PhoneNumberTest {

    @DisplayName("전화번호가 숫자 11자로 구성되어 있지 않으면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"010-1234-1234", "010123412345", "010"})
    void validate_phone_number_format(String value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new PhoneNumber(value))
            .isInstanceOf(MemberException.class)
            .hasMessage("전화번호는 -를 제외한 11자이어야 합니다.");
    }

    @DisplayName("전화번호가 숫자 11자로 구성되면 예외를 발생하지 않는다.")
    @Test
    void success_generate_phone_number() {
        // given
        final String value = "01012341234";

        // when
        // then
        assertDoesNotThrow(() -> new PhoneNumber(value));
    }
}
