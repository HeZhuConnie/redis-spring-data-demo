create table t_shop
(
    id                         VARCHAR(36)      NOT NULL PRIMARY KEY,
    name                       VARCHAR(255),
    type_id                     NUMERIC,
    images                     VARCHAR(255)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_bin;
