package choorai.excuseme.member.application.dto;

import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.Role;
import lombok.Builder;

@Builder
public record LoginResponse(Long id, String username, Role role, String token) {

    public static LoginResponse of(final Member member, final String accessToken) {
        return LoginResponse.builder()
            .id(member.getId())
            .username(member.getUsername())
            .token(accessToken)
            .role(member.getRole())
            .build();
    }
}
