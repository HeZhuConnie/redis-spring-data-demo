create table t_user
(
    id                         VARCHAR(36)      NOT NULL PRIMARY KEY,
    name                       VARCHAR(255),
    phone                      VARCHAR(255)     NOT NULL,
    password                   VARCHAR(255),
    age                        VARCHAR(255)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_bin;
