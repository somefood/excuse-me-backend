package choorai.excuseme.lecture.ui;

import choorai.excuseme.lecture.application.LectureService;
import choorai.excuseme.lecture.domain.dto.LectureDetailResponse;
import choorai.excuseme.lecture.domain.dto.LectureRequest;
import choorai.excuseme.lecture.domain.dto.LectureResponse;
import choorai.excuseme.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures")
    public ResponseEntity<List<LectureResponse>> getLectures() {
        List<LectureResponse> lectureResponses = lectureService.getLectures();
        return new ResponseEntity<>(lectureResponses, HttpStatus.OK);
    }

    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<LectureDetailResponse> getLectureById(@AuthenticationPrincipal(expression = "member") final Member member, @PathVariable(name = "lectureId") final Long lectureId) {
        LectureDetailResponse response = lectureService.getLectureById(member, lectureId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/lectures/{lectureId}")
    public void watchLecture(@AuthenticationPrincipal(expression = "member") final Member member,
                             @PathVariable(name = "lectureId") final Long lectureId,
                             @RequestBody final LectureRequest lectureRequest) {
        lectureService.watchLecture(member, lectureId, lectureRequest);
    }
}
