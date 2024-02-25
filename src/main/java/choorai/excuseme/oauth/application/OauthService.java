package choorai.excuseme.oauth.application;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.dto.SignRequest;
import choorai.excuseme.member.domain.dto.SignResponse;
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
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final GoogleOAuthService googleOAuthService;

    @Value("${oauth.password}")
    private String oauthPassword;

    public SignResponse oAuthLogin(String socialLoginType, final OAuthRequest oAuthRequest) {
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

    private SignResponse getOAuthResponse(final String username) {
        final Optional<Member> findMember = memberRepository.findByUsername(username);
        final SignRequest signRequest = SignRequest.builder()
                .username(username)
                .password(oauthPassword)
                .build();
        if (findMember.isEmpty()) {
            register(signRequest);
        }
        return login(signRequest);
    }

    @Transactional
    private void register(final SignRequest signRequest) {
        final Member newMember = Member.createNormalMember(
                signRequest.getUsername(),
                passwordEncoder.encode(signRequest.getPassword())
        );
        memberRepository.save(newMember);
    }

    private SignResponse login(final SignRequest signRequest) {
        final Member foundMember = memberRepository.findByUsername(signRequest.getUsername())
                .orElseThrow(() -> new OauthException(OauthErrorCode.USERNAME_NOT_FOUND));
        final String accessToken = jwtProvider.createToken(foundMember.getUsername(), foundMember.getRole());

        return SignResponse.builder()
                .id(foundMember.getId())
                .username(foundMember.getUsername())
                .token(accessToken)
                .role(foundMember.getRole())
                .build();
    }
}
