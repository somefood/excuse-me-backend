package choorai.excuseme.member.domain;

import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class PhoneNumber {

    private static final Pattern PHONE_NUMBER_FORMAT = Pattern.compile("^\\d{11}$");

    @Column(name = "phone_number", nullable = false, length = 11)
    private String value;

    public PhoneNumber(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        final Matcher matcher = PHONE_NUMBER_FORMAT.matcher(value);
        if (!matcher.matches()) {
            throw new MemberException(MemberErrorCode.WRONG_PHONE_NUMBER);
        }
    }
}
