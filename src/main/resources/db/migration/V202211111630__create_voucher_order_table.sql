create table tb_voucher_order
(
    id                          BIGINT(20) NOT NULL PRIMARY KEY,
    user_id                     VARCHAR(36) NOT NULL,
    voucher_id                  BIGINT(20) UNSIGNED NOT NULL,
    pay_type                    TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '支付方式 1:金额支付; 2:支付宝; 3:微信;',
    status                      TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '订单状态 1:未支付; 2:已支付; 3:已核销; 4:已取消;',
    create_time                 timestamp NOT NULL DEFAULT current_timestamp,
    pay_time                    timestamp NULL DEFAULT NULL,
    use_time                    timestamp NULL DEFAULT NULL,
    refund_time                 timestamp NULL DEFAULT NULL,
    update_time                 timestamp NOT NULL DEFAULT current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_bin;
