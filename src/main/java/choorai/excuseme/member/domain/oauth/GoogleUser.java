package choorai.excuseme.member.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GoogleUser {

    private String id;
    private String email;
    private Boolean verifiedEmail;
    private String name;
    private String picture;

}
