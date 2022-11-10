create table tb_voucher
(
    id                          bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    shop_id                     bigint(20) UNSIGNED DEFAULT NULL,
    title                       VARCHAR(255) NOT NULL,
    sub_title                   VARCHAR(255) DEFAULT NULL,
    rules                       VARCHAR(1024) DEFAULT NULL,
    pay_value                   bigint(10) UNSIGNED NOT NULL,
    actual_value                bigint(10) UNSIGNED NOT NULL,
    type                        tinyint(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0:普通券; 1:秒杀券;',
    status                      tinyint(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '1:上架; 2:下架; 3:过期;',
    create_time                 timestamp NOT NULL DEFAULT current_timestamp,
    update_time                 timestamp NOT NULL DEFAULT current_timestamp
) ENGINE = InnoDB
  AUTO_INCREMENT=7
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_bin;

create table tb_seckill_voucher
(
    voucher_id                  bigint(20) UNSIGNED NOT NULL PRIMARY KEY,
    stock                       int(20) UNSIGNED NOT NULL,
    begin_time                  timestamp NULL DEFAULT NULL,
    end_time                    timestamp NULL DEFAULT NULL,
    create_time                 timestamp NOT NULL DEFAULT current_timestamp,
    update_time                 timestamp NOT NULL DEFAULT current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_bin;
