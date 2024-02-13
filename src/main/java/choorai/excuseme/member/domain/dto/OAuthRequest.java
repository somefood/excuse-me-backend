package choorai.excuseme.member.domain.dto;

import lombok.Builder;

public record OAuthRequest(String accessToken) {

    @Builder
    public OAuthRequest {

    }
}
