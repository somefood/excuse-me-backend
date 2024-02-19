package choorai.excuseme.member.domain.repository;

import choorai.excuseme.member.domain.Member;
import choorai.excuseme.member.domain.UserName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(UserName username);
}
