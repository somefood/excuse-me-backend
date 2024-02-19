package choorai.excuseme.member.application;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.member.application.dto.LoginRequest;
import choorai.excuseme.member.application.dto.SignRequest;
import choorai.excuseme.member.application.dto.LoginResponse;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public LoginResponse login(final LoginRequest loginRequest) {
        final Member foundMember = memberRepository.findByUsername(loginRequest.id())
            .orElseThrow(() -> new MemberException(MemberErrorCode.USERNAME_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.password(), foundMember.getPassword())) {
            throw new MemberException(MemberErrorCode.WRONG_PASSWORD);
        }

        final String accessToken = jwtProvider.createToken(foundMember.getUsername(), foundMember.getRole());

        return LoginResponse.of(foundMember, accessToken);
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
}
