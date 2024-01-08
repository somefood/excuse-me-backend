CREATE TABLE member
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    username   VARCHAR(255)          NOT NULL,
    password   VARCHAR(255)          NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,

    CONSTRAINT idx_id PRIMARY KEY (id),
    CONSTRAINT idx_username UNIQUE (username)
);
