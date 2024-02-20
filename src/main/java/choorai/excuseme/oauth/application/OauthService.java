package choorai.excuseme.oauth.application;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.dto.SignRequest;
import choorai.excuseme.member.domain.dto.SignResponse;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.member.exception.MemberException;
import choorai.excuseme.oauth.domain.dto.GoogleUser;
import choorai.excuseme.oauth.domain.dto.OAuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public SignResponse oAuthLogin(String socialLoginType, OAuthRequest oAuthRequest) {
        String accessToken = oAuthRequest.accessToken();
        socialLoginType = socialLoginType.toUpperCase();
        if (socialLoginType.equals("GOOGLE")) {
            try {
                GoogleUser googleUser = googleOAuthService.getGoogleUser(accessToken);
                return getOAuthResponse(googleUser.email());
            } catch (Exception e) {
                log.debug("origin Error = {}", e);
                throw new MemberException(MemberErrorCode.FAIL_GOOGLE_OAUTH);
            }
        }
        throw new MemberException(MemberErrorCode.INVALID_SOCIAL_LOGIN_TYPE);
    }

    private SignResponse getOAuthResponse(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        SignRequest signRequest = SignRequest.builder()
                .username(username)
                .password(oauthPassword)
                .build();
        if (findMember.isEmpty()) {
            register(signRequest);
        }
        return login(signRequest);
    }

    @Transactional
    private void register(SignRequest signRequest) {
        Member newMember = Member.createNormalMember(
                signRequest.getUsername(),
                passwordEncoder.encode(signRequest.getPassword())
        );
        memberRepository.save(newMember);
    }

    private SignResponse login(SignRequest signRequest) {
        Member foundMember = memberRepository.findByUsername(signRequest.getUsername())
                .orElseThrow(() -> new MemberException(MemberErrorCode.USERNAME_NOT_FOUND));
        String accessToken = jwtProvider.createToken(foundMember.getUsername(), foundMember.getRole());

        return SignResponse.builder()
                .id(foundMember.getId())
                .username(foundMember.getUsername())
                .token(accessToken)
                .role(foundMember.getRole())
                .build();
    }
}
