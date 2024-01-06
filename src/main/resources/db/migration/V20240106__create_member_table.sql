CREATE TABLE member
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    username varchar(50)                    NOT NULL UNIQUE,
    password varchar(100)                   NOT NULL,
    created_at datetime,
    updated_at datetime
)