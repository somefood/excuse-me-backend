package choorai.excuseme.member.application;

import choorai.excuseme.member.domain.oauth.GoogleUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleOAuthService {

    private static final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ResponseEntity<String> requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class);
        log.info("response.getBody() = {}", response.getBody());
        return response;
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
        return  objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
    }
}
