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
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleOAuthService {

    private static final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleUser getGoogleUser(String accessToken) throws JsonProcessingException {
        ResponseEntity<String> userInfoResponse = requestUserInfo(accessToken);
        String userInfo = userInfoResponse.getBody();
        log.info("Google User Info  = {}", userInfo);
        return objectMapper.readValue(userInfo, GoogleUser.class);
    }

    private ResponseEntity<String> requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class);
    }
}
