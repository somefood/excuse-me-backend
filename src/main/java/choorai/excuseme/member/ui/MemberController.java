package choorai.excuseme.member.ui;

import choorai.excuseme.member.application.MemberService;
import choorai.excuseme.member.domain.dto.SignRequest;
import choorai.excuseme.member.domain.dto.SignResponse;
import choorai.excuseme.member.application.dto.LoginRequest;
import choorai.excuseme.member.application.dto.LoginResponse;
import choorai.excuseme.member.application.dto.SignRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final OauthService oauthService;

    @PostMapping("/register")
    public ResponseEntity<Void> signup(@RequestBody final SignRequest signRequest) {
        memberService.register(signRequest);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest loginRequest) {
        final LoginResponse loginResponse = memberService.login(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/oauth/login/{loginType}")
    public ResponseEntity<SignResponse> oauthLogin(
            @PathVariable("loginType") String socialLoginType,
            @RequestBody OAuthRequest oAuthRequest) {
        SignResponse oAuthResponse = oauthService.oAuthLogin(socialLoginType, oAuthRequest);
        return new ResponseEntity<>(oAuthResponse, HttpStatus.OK);
    }
}
