package choorai.excuseme.member.domain;

import choorai.excuseme.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true)
})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Member createNormalMember(String username, String password) {
        return new Member(username, password, Role.USER);
    }

    public static Member createAdminMember(String username, String password) {
        return new Member(username, password, Role.ADMIN);
    }
}
