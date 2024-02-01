package choorai.excuseme.member.domain.dto;

import choorai.excuseme.member.domain.Role;
import lombok.Builder;

public record SignResponse(Long id, String username, Role role, String token) {

    @Builder
    public SignResponse {
    }
}
