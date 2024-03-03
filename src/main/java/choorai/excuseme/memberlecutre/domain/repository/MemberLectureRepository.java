package choorai.excuseme.memberlecutre.domain.repository;

import choorai.excuseme.lecture.domain.Lecture;
import choorai.excuseme.member.domain.Member;
import choorai.excuseme.memberlecutre.domain.MemberLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberLectureRepository extends JpaRepository<MemberLecture, Long> {

    List<MemberLecture> findByMember(Member member);

    Optional<MemberLecture> findByMemberAndLecture(final Member member, final Lecture lecture);
}
