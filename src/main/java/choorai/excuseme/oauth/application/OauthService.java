package choorai.excuseme.oauth.application;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.member.application.PasswordChecker;
import choorai.excuseme.member.application.dto.LoginResponse;
import choorai.excuseme.member.application.dto.SignRequest;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.UserName;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.oauth.domain.dto.GoogleUser;
import choorai.excuseme.oauth.domain.dto.OAuthRequest;
import choorai.excuseme.oauth.exception.OauthErrorCode;
import choorai.excuseme.oauth.exception.OauthException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {

    private final PasswordEncoder passwordEncoder;
    private final PasswordChecker passwordChecker;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final GoogleOAuthService googleOAuthService;

    // TODO : 클라이언트와 논의 후 처리 필요
    @Value("${oauth.password}")
    private String oauthPassword;

    @Value("${oauth.name}")
    private String oauthName;

    @Value("${oauth.gender}")
    private String oauthGender;

    @Value("${oauth.birthDate}")
    private String oauthBirthDate;

    @Value("${oauth.phoneNumber}")
    private String oauthPhonenumber;

    public LoginResponse oAuthLogin(String socialLoginType, final OAuthRequest oAuthRequest) {
        final String accessToken = oAuthRequest.accessToken();
        socialLoginType = socialLoginType.toUpperCase();
        if (socialLoginType.equals("GOOGLE")) {
            try {
                final GoogleUser googleUser = googleOAuthService.getGoogleUser(accessToken);
                return getOAuthResponse(googleUser.email());
            } catch (Exception e) {
                log.debug("origin Error = {}", e);
                throw new OauthException(OauthErrorCode.FAIL_GOOGLE_OAUTH);
            }
        }
        throw new OauthException(OauthErrorCode.INVALID_SOCIAL_LOGIN_TYPE);
    }

    private LoginResponse getOAuthResponse(final String id) {
        final Optional<Member> findMember = memberRepository.findByUsername(new UserName(id));
        if (findMember.isEmpty()) {
            passwordChecker.validate(oauthPassword);

            final Member newMember = Member.createNormalMember(
                id,
                passwordEncoder.encode(oauthPassword),
                oauthName,
                oauthGender,
                oauthBirthDate,
                oauthPhonenumber
            );
            memberRepository.save(newMember);
            return login(newMember);
        }
        return login(findMember.get());
    }

    @Transactional
    public void register(final SignRequest signRequest) {
        final Member newMember = Member.createNormalMember(
            signRequest.id(),
            passwordEncoder.encode(signRequest.password()),
            signRequest.name(),
            signRequest.gender(),
            signRequest.birthDate(),
            signRequest.phoneNumber()
        );
        memberRepository.save(newMember);
    }

    private LoginResponse login(final Member member) {
        final String accessToken = jwtProvider.createToken(member.getUsername(), member.getRole());
        return LoginResponse.of(member, accessToken);
    }
}
