package choorai.excuseme.member.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class KakaoUser {

    private String email;

}
