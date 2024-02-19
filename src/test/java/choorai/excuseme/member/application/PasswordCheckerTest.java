package choorai.excuseme.member.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import choorai.excuseme.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordCheckerTest {

    @DisplayName("비밀번호가 숫자, 영어, 특수문자가 모두 포함되지 않고 길이가 6자와 20사이가 아니면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"aaaaaa", "111111", "!!!!!!", "a!1as", "a@sa@1s4bvfcdsxz45gs2"})
    void validate_password_format(String value) {
        // given
        final PasswordChecker passwordChecker = new PasswordChecker();

        // when
        // then
        assertThatThrownBy(() -> passwordChecker.validate(value))
            .isInstanceOf(MemberException.class)
            .hasMessage("비밀번호는 숫자, 영어, 특수문자를 모두 포함한 6자에서 20자이어야 합니다.");
    }

    @DisplayName("비밀번호가 올바른 형태(숫자, 영어, 특수문자 포함 6자 이상 20자 이하)인 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"a@1s43", "asbcdsxcdf@341sdfcxa"})
    void success_check_password(String value) {
        // given
        final PasswordChecker passwordChecker = new PasswordChecker();

        // when
        // then
        assertDoesNotThrow(() -> passwordChecker.validate(value));
    }
}
