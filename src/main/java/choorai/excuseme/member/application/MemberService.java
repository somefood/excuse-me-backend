package choorai.excuseme.member.application;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.dto.SignRequest;
import choorai.excuseme.member.domain.dto.SignResponse;
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

    public SignResponse login(SignRequest signRequest) {

        Member foundMember = memberRepository.findByUsername(signRequest.getUsername())
                .orElseThrow(() -> new MemberException(MemberErrorCode.USERNAME_NOT_FOUND));

        if (!passwordEncoder.matches(signRequest.getPassword(), foundMember.getPassword())) {
            throw new MemberException(MemberErrorCode.WRONG_PASSWORD);
        }

        String accessToken = jwtProvider.createToken(foundMember.getUsername(), foundMember.getRole());

        return SignResponse.builder()
                .id(foundMember.getId())
                .username(foundMember.getUsername())
                .token(accessToken)
                .role(foundMember.getRole())
                .build();
    }

    @Transactional
    public void register(SignRequest signRequest) {

        if (memberRepository.findByUsername(signRequest.getUsername()).isPresent()) {
            throw new MemberException(MemberErrorCode.ALREADY_EXIST);
        }

        Member newMember = Member.createNormalMember(
                signRequest.getUsername(),
                passwordEncoder.encode(signRequest.getPassword())
        );

        memberRepository.save(newMember);
    }
}
