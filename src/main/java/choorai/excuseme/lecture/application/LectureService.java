package choorai.excuseme.lecture.application;

import choorai.excuseme.lecture.domain.Lecture;
import choorai.excuseme.lecture.domain.dto.LectureDetailResponse;
import choorai.excuseme.lecture.domain.dto.LectureRequest;
import choorai.excuseme.lecture.domain.dto.LectureResponse;
import choorai.excuseme.lecture.domain.repository.LectureRepository;
import choorai.excuseme.lecture.exception.LectureErrorCode;
import choorai.excuseme.lecture.exception.LectureException;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.exception.MemberErrorCode;
import choorai.excuseme.member.exception.MemberException;
import choorai.excuseme.memberlecutre.domain.MemberLecture;
import choorai.excuseme.memberlecutre.domain.repository.MemberLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberLectureRepository memberLectureRepository;

    public List<LectureResponse> getLectures() {
        List<Lecture> lectures = lectureRepository.findAll();

        return lectures.stream()
                .map(l -> new LectureResponse(l.getId(), l.getName()))
                .toList();
    }

    public LectureDetailResponse getLectureById(final Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException(LectureErrorCode.LECTURE_NOT_FOUND));

        return new LectureDetailResponse(
                lecture.getId(),
                lecture.getName(),
                lecture.getThumbnail(),
                lecture.getVideoUrl()
        );
    }

    @Transactional
    public void watchLecture(final Member member, final Long lectureId, final LectureRequest lectureRequest) {

        if (member == null) {
            throw new MemberException(MemberErrorCode.USERNAME_NOT_FOUND);
        }

        Lecture foundLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException(LectureErrorCode.LECTURE_NOT_FOUND));

        MemberLecture memberLecture = memberLectureRepository.findByMemberAndLecture(member, foundLecture)
                .orElseGet(() -> memberLectureRepository.save(new MemberLecture(member, foundLecture)));

        memberLecture.changeProgressStatus(lectureRequest.getProgressStatus());
    }
}
