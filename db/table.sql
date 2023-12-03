INSERT INTO `shop_info`
(type, shop_title, shop_describe, number, effective_days, price, feature)
VALUES (1, '包年不限次', '限时每日赠送20次4.0体验
低至1.09元/天', -99, 365, 399.99, NULL),
       (1, '包季不限次', '限时每日赠送20次4.0体验
低至1.43元/天', -99, 90, 129.99, NULL),
       (1, '包月不限次', '限时每日赠送20次4.0体验
低至1.63元/天', -99, 30, 49.99, NULL),
       (1, '三天不限次', '限时每日赠送20次4.0体验
3.3元/天', -99, 3, 9.99, NULL);


update shop_info set shop_describe = '限时每日赠送20次4.0体验
低至1.09元/天' where id = 100000;
update shop_info set shop_describe = '限时每日赠送20次4.0体验
低至1.43元/天' where id = 100001;
update shop_info set shop_describe = '限时每日赠送20次4.0体验
低至1.43元/天' where id = 100002;
update shop_info set shop_describe = '限时每日赠送20次4.0体验
3.3元/天' where id = 100003;



INSERT INTO `shop_info`
(type, shop_title, shop_describe, number, effective_days, price, feature)
VALUES (2, '画图月会员', '200组极速+无限放松模式', 200, 30, 49.99, NULL),
       (2, '画图季会员', '800组极速+无限放松模式', 800, 90, 129.99, NULL),
       (2, '画图包年会员', '4000组极速+无限放松模式', 4000, 365, 399.99, NULL);


INSERT INTO `shop_info`
(type, shop_title, shop_describe, number, effective_days, price, feature)
VALUES (3, '4.0加量包10次', '总共10次', 10, 365, 9.99, NULL),
       (3, '4.0加量包100次', '总共100次', 100, 365, 89.99, NULL),
       (3, '4.0加量包1000次', '总共1000次', 1000, 365, 799.99, NULL);



alter table purchase_record modify `effective_days` SMALLINT NOT NULL COMMENT '有效天数';


update user_vip_info set type=1, package_type=2, total_number=-99,  text_remain_number=-99, expired_time='2024-02-22 21:41:29' where user_id = 100001;
update user_vip_info set type=1, package_type=2, total_number=-99,  text_remain_number=-99, expired_time='2023-11-24 22:38:59' where user_id = 100002;
update user_vip_info set type=1, package_type=2, total_number=-99,  text_remain_number=-99, expired_time='2024-08-25 21:45:10' where user_id = 100003;
update user_vip_info set type=1, package_type=2, total_number=-99,  text_remain_number=-99, expired_time='2023-08-26 21:56:17' where user_id = 100004;
update user_vip_info set type=1, package_type=2, total_number=-99,  text_remain_number=-99, expired_time='2024-02-22 22:11:02' where user_id = 100007;


