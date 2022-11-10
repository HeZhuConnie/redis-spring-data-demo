INSERT INTO tb_voucher (shop_id, title, sub_title, rules, pay_value, actual_value, type) VALUES
(1,'50元代金券', '周一至周日可使用', '全场通用', 4750, 5000, 0),
(1,'100元代金券', '周一至周日可使用', '全场通用', 8000, 10000, 1);


INSERT INTO tb_seckill_voucher (voucher_id, stock, begin_time, end_time) VALUES
(7, 100, current_timestamp, current_timestamp);
