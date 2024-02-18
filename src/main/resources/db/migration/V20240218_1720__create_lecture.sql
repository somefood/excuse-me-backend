CREATE TABLE lecture
(
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name       VARCHAR(255)          NOT NULL,
    thumbnail  VARCHAR(255)          NOT NULL,
    video_url  VARCHAR(255)          NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
