package choorai.excuseme.global.security;

import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.UserName;
import choorai.excuseme.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(new UserName(username))
            .orElseThrow(() -> new UsernameNotFoundException("Invalid authentication"));

        return new CustomUserDetails(member);
    }
}
