package choorai.excuseme.memberlecutre.domain;

import choorai.excuseme.global.domain.BaseEntity;
import choorai.excuseme.lecture.domain.Lecture;
import choorai.excuseme.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberLecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_member"))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lecture_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_lecture"))
    private Lecture lecture;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private ProgressStatus progressStatus;
}
