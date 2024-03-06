package choorai.excuseme.oauth.application;

import choorai.excuseme.oauth.domain.dto.GoogleUser;
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

    public GoogleUser getGoogleUser(final String accessToken) {
        final ResponseEntity<GoogleUser> userInfoResponse = requestUserInfo(accessToken);
        final GoogleUser googleUser = userInfoResponse.getBody();
        log.info("Google User Info  = {}", googleUser);
        return googleUser;
    }

    private ResponseEntity<GoogleUser> requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                GoogleUser.class);
    }
}
