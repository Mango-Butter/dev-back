INSERT INTO user (user_id, email, name, password, phone, kakao_id, birth, profile_image_url, role, created_at,
                  modified_at)
VALUES (1, 'test1@ajou.ac.kr', '망사장', null, '010-1234-5678', '11111111', '2000-02-13',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'BOSS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'test2@ajou.ac.kr', '망알바', null, '010-2312-1111', '22222222', '2006-05-11',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'test3@ajou.ac.kr', '망알바2', null, '010-4972-4844', '33333333', '2004-07-22',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'test4@ajou.ac.kr', '망알바3', null, '010-3993-2222', '44444444', '2005-11-30',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        'STAFF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO store (store_id, boss_id, name, address, business_number, store_type, invite_code,
                   attendance_method, gps_range_meters, gps_latitude, gps_longitude, qr_code, created_at, modified_at)
VALUES (1, 1, '망고쥬스', '경기도 수원시 영통구 월드컵로 206', '1248210324', 'CAFE', 'abc123', 'QR', 10, 37.2820, 127.0463,
        '123456abcdef', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO staff (staff_id, user_id, store_id, name, profile_image_url, created_at, modified_at)
VALUES (1, 2, 1, '망알바',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 3, 1, '망알바2',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 4, 1, '망알바3',
        'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMTBfODAg/MDAxNTgxMzA0MTE3ODMy.ACRLtB9v5NH-I2qjWrwiXLb7TeUiG442cJmcdzVum7cg.eTLpNg_n0rAS5sWOsofRrvBy0qZk_QcWSfUiIagTfd8g.JPEG.lattepain/1581304118739.jpg?type=w800',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
