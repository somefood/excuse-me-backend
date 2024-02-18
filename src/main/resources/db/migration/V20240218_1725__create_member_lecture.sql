CREATE TABLE member_lecture
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    member_id       BIGINT                NOT NULL,
    lecture_id      BIGINT                NOT NULL,
    progress_status VARCHAR(255)          NOT NULL,
    created_at      DATETIME,
    updated_at      DATETIME,

    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_lecture FOREIGN KEY (lecture_id) REFERENCES lecture (id)
);
