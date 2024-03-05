package choorai.excuseme.member.ui;

import static org.assertj.core.api.Assertions.assertThat;

import choorai.excuseme.global.exception.dto.CustomExceptionResponse;
import choorai.excuseme.member.application.dto.LoginRequest;
import choorai.excuseme.member.application.dto.LoginResponse;
import choorai.excuseme.member.application.dto.SignRequest;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.UserName;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.support.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberControllerTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanData() {
        memberRepository.deleteAll();
    }

    @DisplayName("일반 회원가입을 성공한다.")
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
        final Member registeredMember = memberRepository.findByUsername(new UserName(request.id())).get();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(registeredMember.getUsername()).isEqualTo(request.id());
            softAssertions.assertThat(passwordEncoder.matches(request.password(), registeredMember.getPassword())).isTrue();
            softAssertions.assertThat(registeredMember.getName()).isEqualTo(request.name());
            softAssertions.assertThat(registeredMember.getGender().name()).isEqualTo(request.gender());
            softAssertions.assertThat(registeredMember.getBirthDate()).isEqualTo(
                LocalDate.parse(request.birthDate(), DateTimeFormatter.ofPattern("yyyyMMdd")));
            softAssertions.assertThat(registeredMember.getPhoneNumber()).isEqualTo(request.phoneNumber());
        });
    }

    @DisplayName("이메일 형식이 아닌 id 입력을 받으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"id", "as!asds.asc", "as@asdsc.sdsa"})
    void fail_register_with_wrongId(String id) {
        // given
        final SignRequest request = new SignRequest(id,
                                                    "password1@",
                                                    "이름",
                                                    "MEN",
                                                    "20240219",
                                                    "01012341234");

        // when
        final CustomExceptionResponse result = RestAssured
            .given().body(request).contentType(ContentType.JSON)
            .when().post("/members/register")
            .then().statusCode(HttpStatus.BAD_REQUEST.value())
            .extract().body().as(CustomExceptionResponse.class);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.code()).isEqualTo(MemberErrorCode.WRONG_USERNAME.getCode());
            softAssertions.assertThat(result.errorMessage()).isEqualTo(MemberErrorCode.WRONG_USERNAME.getMessage());
        });
    }

    @DisplayName("문자, 숫자, 특수문자를 포함한 6자 이상 20자 이하가 아닌 비밀번호 입력을 받으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"12345678902", "@!@#%$&^#!@", "absdcsdesgs", "as!asds.asc", "a1s3f4c2b5s", "!2#4%6&8@1!",
        "ds@a1", "ddksc@123asvs@123ddksc2"})
    void fail_register_with_wrongPassword(String password) {
        // given
        final SignRequest request = new SignRequest("a@email.com",
                                                    password,
                                                    "이름",
                                                    "MEN",
                                                    "20240219",
                                                    "01012341234");

        // when
        final CustomExceptionResponse result = RestAssured
            .given().body(request).contentType(ContentType.JSON)
            .when().post("/members/register")
            .then().statusCode(HttpStatus.BAD_REQUEST.value())
            .extract().body().as(CustomExceptionResponse.class);

        // then
        final MemberErrorCode expect = MemberErrorCode.WRONG_PASSWORD;

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.code()).isEqualTo(expect.getCode());
            softAssertions.assertThat(result.errorMessage()).isEqualTo(expect.getMessage());
        });
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
