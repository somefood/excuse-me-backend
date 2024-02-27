package choorai.excuseme.lecture.ui;

import choorai.excuseme.lecture.application.LectureService;
import choorai.excuseme.lecture.domain.dto.LectureDetailResponse;
import choorai.excuseme.lecture.domain.dto.LectureRequest;
import choorai.excuseme.lecture.domain.dto.LectureResponse;
import choorai.excuseme.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures")
    public ResponseEntity<List<LectureResponse>> getLectures() {
        final List<LectureResponse> lectureResponses = lectureService.getLectures();
        return ResponseEntity.ok(lectureResponses);
    }

    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<LectureDetailResponse> getLectureById(@PathVariable(name = "lectureId") final Long lectureId) {
        final LectureDetailResponse response = lectureService.getLectureById(lectureId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/lectures/{lectureId}")
    public void watchLecture(@AuthenticationPrincipal(expression = "member") final Member member,
                             @PathVariable(name = "lectureId") final Long lectureId,
                             @RequestBody final LectureRequest lectureRequest) {
        lectureService.watchLecture(member, lectureId, lectureRequest);
    }
}
