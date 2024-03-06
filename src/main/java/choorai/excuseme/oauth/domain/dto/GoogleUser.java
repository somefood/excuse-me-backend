package choorai.excuseme.oauth.domain.dto;

import lombok.Builder;

public record GoogleUser(String id, String email, Boolean verifiedEmail, String name, String picture) {

    @Builder
    public GoogleUser {
    }
}
