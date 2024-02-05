package choorai.excuseme.member.application;

import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.dto.OAuthRequest;
import choorai.excuseme.member.domain.dto.SignRequest;
import choorai.excuseme.member.domain.dto.SignResponse;
import choorai.excuseme.member.domain.oauth.GoogleUser;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final GoogleOAuthService googleOAuthService;


    public SignResponse oAuthLogin(String socialLoginType, OAuthRequest oAuthRequest) {
        String accessToken = oAuthRequest.getAccessToken();
        socialLoginType = socialLoginType.toUpperCase();
        if (socialLoginType.equals("GOOGLE")) {
            try {
                GoogleUser googleUser = googleOAuthService.getGoogleUser(accessToken);
                log.info("get GoogleUserInfo = {}", googleUser);
                return getOAuthResponse(googleUser.getEmail());
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
                .password("")
                .build();
        if (findMember.isEmpty()) {
            memberService.register(signRequest);
        }
        return memberService.login(signRequest);
    }
}
