package choorai.excuseme.member.application;

import choorai.excuseme.member.domain.oauth.KakaoUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {

    private static final String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoUser getKakaoUser(String accessToken) throws JsonProcessingException {
        ResponseEntity<String> userInfoResponse = requestUserInfo(accessToken);
        String email = getEmail(userInfoResponse);
        return KakaoUser.builder()
                .email(email)
                .build();
    }

    // TODO : kakao 프로필에서 가져올 추가 정보 필요하다면 해당 메서드 수정 + 명칭도 수정
    private String getEmail(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
        String userInfoResponseBody = userInfoResponse.getBody();
        JsonNode userInfoTree = objectMapper.readTree(userInfoResponseBody);
        String email = userInfoTree.get("kakao_account")
                        .get("email").asText();
        log.info("Google User Info  = {}", userInfoTree);
        return email;
    }

    private ResponseEntity<String> requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                KAKAO_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class);
    }
}
