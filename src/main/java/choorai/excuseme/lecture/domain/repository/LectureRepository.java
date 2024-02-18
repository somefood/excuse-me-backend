package choorai.excuseme.lecture.domain.repository;

import choorai.excuseme.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
