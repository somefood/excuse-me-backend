package choorai.excuseme.member.application;

import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.member.exception.MemberException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PasswordChecker {

    // 숫자, 영문자, 특수문자를 모두 포함하여 6자이상 20자이하 정규식
    private static final Pattern PASSWORD_FORMAT = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\p{Punct}).{6,20}$");

    public void validate(final String value) {
        final Matcher matcher = PASSWORD_FORMAT.matcher(value);
        if (!matcher.matches()) {
            throw new MemberException(MemberErrorCode.WRONG_PASSWORD);
        }
    }
}
