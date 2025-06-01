INSERT INTO user (user_id, email, name, password, phone, kakao_id, birth, profile_image_url, role,
                  created_at, modified_at)
VALUES (1, 'test1@ajou.ac.kr', '망고보스', null, '010-1234-5678', '11111111', '2000-02-13',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'BOSS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'test2@ajou.ac.kr', '망알바', null, '010-2312-1111', '22222222', '2006-05-11',
        'http://k.kakaocdn.net/dn/KNfQc/btsNkI4rZDT/FqS6EhArmExoRcVX9bane1/img_110x110.jpg',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'test3@ajou.ac.kr', '고알바', null, '010-4972-4844', '33333333', '2004-07-22',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'test4@ajou.ac.kr', '보알바', null, '010-3993-2222', '44444444', '2005-11-30',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 'test5@ajou.ac.kr', '스알바', null, '010-7777-7777', '55555555', '2000-11-20',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (6, 'test6@ajou.ac.kr', '정알바', null, '010-7712-2367', '66666666', '1997-09-01',
        'http://k.kakaocdn.net/dn/bD4Rih/btsNXAxY4w1/XuxLt8jAsnIA5ZaHVjkE0k/img_110x110.jpg',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO store (store_id, boss_id, name, address, business_number, store_type, invite_code,
                   attendance_method, gps_range_meters, gps_latitude, gps_longitude, qr_code, overtime_limit,
                   created_at, modified_at)
VALUES (1, 1, '망고쥬스', '경기도 수원시 영통구 월드컵로 206', '1248210324', 'CAFE', 'ABC123', 'QR', 10, 37.2843727, 127.0443767,
        '123456ABCDEF', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, '망고코코넛', '경기 수원시 팔달구 아주로 37', '1248210324', 'CAFE', '3A4C78', 'QR', 15, 37.2822024, 127.0463244,
        'JUADEFSE2392', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 1, '스몰하노이', '경기도 고양시 일산서구 성저로 92', '1953100092', 'RESTAURANT', 'BJJ38L', 'QR', 15, 37.685024, 126.757285,
        'KOP22WT78JUC',0,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO transfer_account (transfer_id, bank_code, account_number, account_holder, fin_account,
                              created_at, modified_at)
VALUES (1, 'NH', '3020000012816', '망고보스', '00820100029430000000000027417', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO payroll_setting (payroll_setting_id, auto_transfer_enabled, deduction_unit, commuting_allowance,
                             transfer_date, transfer_account_id, store_id)
VALUES (1, true, 10, 3000, 20, 1, 1),
       (2, false, 10, 0, null, null, 2),
       (3, false, 10, 0, null, null, 3);

INSERT INTO required_document (store_id, document_type, is_required, created_at, modified_at)
VALUES (1, 'RESIDENT_REGISTRATION', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'BANK_ACCOUNT', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'ID_CARD', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'HEALTH_CERTIFICATE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'RESIDENT_REGISTRATION', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'BANK_ACCOUNT', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'ID_CARD', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'HEALTH_CERTIFICATE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'RESIDENT_REGISTRATION', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'BANK_ACCOUNT', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'ID_CARD', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'HEALTH_CERTIFICATE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO staff (staff_id, user_id, store_id, name, profile_image_url, withholding_type,
                   hourly_wage, bank_code, account,
                   created_at, modified_at)
VALUES (1, 2, 1, '망알바',
        'http://k.kakaocdn.net/dn/KNfQc/btsNkI4rZDT/FqS6EhArmExoRcVX9bane1/img_110x110.jpg',
        'INCOME_TAX', 10030, 'NH', '3020000013133', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 3, 1, '고알바',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'INCOME_TAX', 10030, 'NH', '3020000013093', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 4, 1, '보알바',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'NONE', 10030, 'NH', '3020000013132', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 5, 1, '스알바',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'SOCIAL_INSURANCE', 10030, 'NH', '3020000013095', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 6, 1, '정알바',
        'http://k.kakaocdn.net/dn/bD4Rih/btsNXAxY4w1/XuxLt8jAsnIA5ZaHVjkE0k/img_110x110.jpg',
        'INCOME_TAX', 10030, 'NH', '3020000013094', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO schedule (schedule_id, staff_id, regular_group_id, work_date, start_time, end_time, store_id, state)
VALUES (1, 1, null, '2025-04-01', '2025-04-01 09:00:00', '2025-04-01 15:30:00', 1, 'NONE'),
       (2, 2, null, '2025-05-01', '2025-05-01 13:00:00', '2025-05-01 18:00:00', 1, 'NONE'),
       (3, 1, null, '2025-05-03', '2025-05-03 10:00:00', '2025-05-03 18:00:00', 1, 'NONE'),
       (4, 2, null, '2025-05-03', '2025-05-03 10:00:00', '2025-05-03 18:00:00', 1, 'NONE'),
       (5, 1, null, '2025-05-05', '2025-05-05 09:00:00', '2025-05-05 14:30:00', 1, 'NONE'),
       (6, 3, null, '2025-05-07', '2025-05-07 10:00:00', '2025-05-07 14:00:00', 1, 'NONE'),
       (7, 5, null, '2025-05-08', '2025-05-08 09:00:00', '2025-05-08 14:00:00', 1, 'NONE'),
       (8, 4, null, '2025-05-10', '2025-05-10 10:00:00', '2025-05-10 14:00:00', 1, 'NONE'),
       (9, 1, null, '2025-05-10', '2025-05-10 10:00:00', '2025-05-10 14:00:00', 1, 'NONE'),
       (10, 1, null, '2025-04-01', '2025-04-01 09:00:00', '2025-04-01 15:00:00', 1, 'NONE'),
       (11, 2, null, '2025-04-02', '2025-04-02 10:00:00', '2025-04-02 16:00:00', 1, 'NONE'),
       (12, 3, null, '2025-04-03', '2025-04-03 13:00:00', '2025-04-03 17:00:00', 1, 'NONE'),
       (13, 4, null, '2025-04-04', '2025-04-04 14:00:00', '2025-04-04 18:00:00', 1, 'NONE'),
       (14, 5, null, '2025-04-05', '2025-04-05 09:00:00', '2025-04-05 13:00:00', 1, 'NONE'),
       (15, 1, null, '2025-04-06', '2025-04-06 11:00:00', '2025-04-06 17:00:00', 1, 'NONE'),
       (16, 2, null, '2025-04-07', '2025-04-07 08:30:00', '2025-04-07 14:30:00', 1, 'NONE');

INSERT INTO attendance(attendance_id, schedule_id, clock_in_time, clock_out_time, clock_in_status, clock_out_status)
VALUES (1, 1, '2025-04-01 09:00:00', '2025-04-01 15:30:00', 'NORMAL', 'NORMAL'),
       (2, 2, '2025-05-01 13:00:00', '2025-05-01 18:00:00', 'NORMAL', 'NORMAL'),
       (3, 3, '2025-05-03 10:05:00', '2025-05-03 18:00:00', 'LATE', 'NORMAL'),
       (4, 4, null, null, 'ABSENT', 'ABSENT'),
       (5, 5, '2025-05-05 09:00:00', '2025-05-05 14:30:00', 'NORMAL', 'NORMAL'),
       (6, 6, '2025-05-07 10:00:00', '2025-05-07 13:00:00', 'NORMAL', 'EARLY_LEAVE'),
       (7, 7, '2025-05-08 09:00:00', '2025-05-08 14:20:00', 'NORMAL', 'OVERTIME'),
       (8, 8, null, null, 'ABSENT', 'ABSENT'),
       (9, 10, '2025-04-01 09:00:00', '2025-04-01 15:00:00', 'NORMAL', 'NORMAL'),
       (10, 11, '2025-04-02 10:00:00', '2025-04-02 16:00:00', 'NORMAL', 'NORMAL'),
       (11, 12, '2025-04-03 13:00:00', '2025-04-03 17:00:00', 'NORMAL', 'NORMAL'),
       (12, 13, '2025-04-04 14:00:00', '2025-04-04 18:00:00', 'NORMAL', 'NORMAL'),
       (13, 14, '2025-04-05 09:00:00', '2025-04-05 13:00:00', 'NORMAL', 'NORMAL'),
       (14, 15, '2025-04-06 11:00:00', '2025-04-06 17:00:00', 'NORMAL', 'NORMAL'),
       (15, 16, '2025-04-07 08:30:00', '2025-04-07 14:30:00', 'NORMAL', 'NORMAL');