package choorai.excuseme.lecture.ui;

import static org.assertj.core.api.Assertions.assertThat;

import choorai.excuseme.global.security.JwtProvider;
import choorai.excuseme.lecture.domain.Lecture;
import choorai.excuseme.lecture.domain.dto.LectureRequest;
import choorai.excuseme.lecture.domain.repository.LectureRepository;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.Role;
import choorai.excuseme.member.domain.repository.MemberRepository;
import choorai.excuseme.memberlecutre.domain.MemberLecture;
import choorai.excuseme.memberlecutre.domain.ProgressStatus;
import choorai.excuseme.memberlecutre.domain.repository.MemberLectureRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LectureControllerTest {

    @LocalServerPort
    private int port;
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

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void afterEach() {
        memberLectureRepository.deleteAll();
        lectureRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("강의를 시청한다.")
    void watch_lecture() throws Exception {
        // given
        final Member normalMember = Member.createNormalMember("excuseme", passwordEncoder.encode("excuseme"));
        memberRepository.save(normalMember);

        final Lecture lecture = new Lecture("lecture1", "thumbnail", "videoUrl");
        lectureRepository.save(lecture);

        final String accessToken = jwtProvider.createToken("excuseme", Role.USER);
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

        assertThat(registeredMemberLecture).isNotEmpty();

        final MemberLecture memberLecture = registeredMemberLecture.get();

        assertThat(memberLecture.getProgressStatus()).isEqualTo(ProgressStatus.COMPLETED);
    }
}
