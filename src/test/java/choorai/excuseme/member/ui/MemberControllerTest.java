package choorai.excuseme.member.ui;

import static org.assertj.core.api.Assertions.assertThat;

import choorai.excuseme.member.application.dto.LoginRequest;
import choorai.excuseme.member.application.dto.LoginResponse;
import choorai.excuseme.member.application.dto.SignRequest;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.UserName;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.support.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class MemberControllerTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanData() {
        memberRepository.deleteAll();
    }

    @DisplayName("일반 회원가입을 수행한다.")
    @Test
    void register_member() {
        // given
        final SignRequest request = new SignRequest("a@email.com",
                                                    "password1@",
                                                    "이름",
                                                    "MEN",
                                                    "20240219",
                                                    "01012341234");

        // when
        RestAssured
            .given().body(request).contentType(ContentType.JSON)
            .when().post("/members/register")
            .then().statusCode(HttpStatus.CREATED.value());

        // then
        final Optional<Member> registeredMember = memberRepository.findByUsername(new UserName(request.id()));
        assertThat(registeredMember).isNotEmpty();
    }

    @DisplayName("일반 로그인을 수행한다.")
    @Test
    void login_member() {
        // given
        final SignRequest registerRequest = new SignRequest("a@email.com",
                                                            "1password1!",
                                                            "이름",
                                                            "MEN",
                                                            "20240219",
                                                            "01012341234");
        RestAssured
            .given().body(registerRequest).contentType(ContentType.JSON)
            .when().post("/members/register");

        final LoginRequest request = new LoginRequest(registerRequest.id(), registerRequest.password());

        // when
        final LoginResponse response = RestAssured
            .given().body(request).contentType(ContentType.JSON)
            .when().post("members/login")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(LoginResponse.class);

        // then
        assertThat(response.username()).isEqualTo(request.id());
    }
}
