package choorai.excuseme.lecture.ui;

import static org.assertj.core.api.Assertions.assertThat;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.lecture.domain.Lecture;
import choorai.excuseme.lecture.domain.dto.LectureDetailResponse;
import choorai.excuseme.lecture.domain.dto.LectureRequest;
import choorai.excuseme.lecture.domain.dto.LectureResponse;
import choorai.excuseme.lecture.domain.repository.LectureRepository;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.Role;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.memberlecutre.domain.MemberLecture;
import choorai.excuseme.memberlecutre.domain.ProgressStatus;
import choorai.excuseme.memberlecutre.domain.repository.MemberLectureRepository;
import choorai.excuseme.support.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

class LectureControllerTest extends AcceptanceTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private MemberLectureRepository memberLectureRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        memberLectureRepository.deleteAll();
        lectureRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 강의 목록을 조회한다.")
    void find_all_lectures() throws Exception {
        // given
        final Member normalMember = Member.createNormalMember(
            "a@email.com",
            passwordEncoder.encode("password1@"),
            "이름",
            "MEN",
            "19990101",
            "01012345678"
        );
        memberRepository.save(normalMember);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            lectureRepository.save(new Lecture("lecture" + i, "thumbnail" + i, "videoUrl" + i));
        });

        final String accessToken = jwtProvider.createToken("a@email.com", Role.USER);

        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().headers("Authorization", "Bearer " + accessToken)
            .when().get("/lectures")
            .then().statusCode(HttpStatus.OK.value())
            .extract();

        // then
        final List<LectureResponse> lectureResponses = response.jsonPath().getList("", LectureResponse.class);
        assertThat(lectureResponses.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("강의를 시청한다.")
    void watch_lecture() throws Exception {
        // given
        final Member normalMember = Member.createNormalMember(
            "a@email.com",
            passwordEncoder.encode("password1@"),
            "이름",
            "MEN",
            "19990101",
            "01012345678"
        );
        memberRepository.save(normalMember);

        final Lecture lecture = new Lecture("lecture1", "thumbnail", "videoUrl");
        lectureRepository.save(lecture);

        final String accessToken = jwtProvider.createToken("a@email.com", Role.USER);
        final LectureRequest lectureRequest = new LectureRequest(ProgressStatus.COMPLETED);

        // when
        RestAssured
            .given()
            .headers("Authorization", "Bearer " + accessToken)
            .body(lectureRequest).contentType(ContentType.JSON)
            .when().post("/lectures/" + lecture.getId())
            .then().statusCode(HttpStatus.OK.value());

        // then
        final Optional<MemberLecture> registeredMemberLecture = memberLectureRepository.findByMemberAndLecture(
            normalMember,
            lecture);

        final MemberLecture memberLecture = registeredMemberLecture.get();
        assertThat(memberLecture.getProgressStatus()).isEqualTo(ProgressStatus.COMPLETED);
    }
}
