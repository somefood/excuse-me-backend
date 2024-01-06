package choorai.excuseme.member.domain.repository;

import choorai.excuseme.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
