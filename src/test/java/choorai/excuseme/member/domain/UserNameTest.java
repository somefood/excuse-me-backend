package choorai.excuseme.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import choorai.excuseme.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserNameTest {

    @DisplayName("username이 이메일 형식이 아니면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"asd", "aaa!try.com", "aaa@try.a", "@try.com"})
    void validate_username_format(String value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new UserName(value))
            .isInstanceOf(MemberException.class)
            .hasMessage("이메일 형식이 아닙니다.");
    }

    @DisplayName("username이 이메일 형식인 경우 예외를 발생하지 않는다.")
    @Test
    void success_generate_username() {
        // given
        final String value = "aaa@try.com";

        // when
        // then
        assertDoesNotThrow(() -> new UserName(value));
    }
}
