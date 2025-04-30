package com.mangoboss.app.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorInfo {

    // 400 Bad Request
    AUTHORIZATION_CODE_MISSING(400, "Authorization code가 비어있습니다.", 400001),
    KAKAO_USER_INFO_REQUEST_FAILED(400, "카카오 사용자 정보 요청 실패", 400002),
    KAKAO_TOKEN_PARSING_FAILED(400, "카카오 토큰 파싱 실패", 400003),
    KAKAO_TOKEN_RESPONSE_EMPTY(400, "카카오 토큰 응답이 비어 있습니다.", 400004),
    URL_INPUT_ERROR(400, "URL 입력 오류입니다.", 400005),
    ILLEGAL_ARGUMENT_TOKEN(400, "토큰 형식이 잘못되었습니다.", 400006),
    KAKAO_USER_INFO_INCOMPLETE(400, "카카오에서 받아오는 정보 중 일부가 누락되었습니다.", 400007),
    INVALID_BUSINESS_NUMBER(400, "유효하지 않은 사업자등록번호입니다.", 400008),
    INVALID_SCHEDULE_TIME(400,"스케줄의 시간이 유효하지 않습니다.",400009),
    INVALID_REGULAR_DATE(400,"반복 날짜가 유효하지 않습니다.",400010),
    ATTENDANCE_METHOD_NONE_SELECTED(400, "출퇴근 방식을 하나 이상 선택해주세요.", 400011),
    INVALID_QR_CODE(400, "유효하지 않은 QR 코드입니다.", 400011),
    INVALID_GPS_TIME(400, "위치 정보 유효 시간이 지났습니다.", 400012),
    GPS_OUT_OF_RANGE(400, "출근 반경을 벗어났습니다.", 400013),
    CHECKIN_TOO_EARLY(400, "출근 가능 시간보다 이르게 시도되었습니다.", 400014),
    CHECKIN_TOO_LATE(400, "이미 퇴근 시간이 지나 출근할 수 없습니다.", 400015),
    INVALID_ATTENDANCE_REQUEST_TYPE(400, "출근 방식에 맞지 않는 요청 형식입니다. 매장의 출근 방식에 따라 필요한 필드를 확인해주세요.", 40016),
    ATTENDANCE_METHOD_CHANGED(400, "출근 인증 방식이 변경되었습니다. 새로고침 후 다시 시도해주세요.", 400017),
    INVALID_ATTENDANCE_METHOD(400, "유효하지 않은 출퇴근 인증 방식입니다.", 400019),

    // 401 Unauthorized
    LOGIN_NEEDED(401, "로그인이 필요합니다.", 401001),
    UNAUTHORIZED(401, "인증에 실패하였습니다.", 401002),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", 401003),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다.", 401004),
    UNSUPPORTED_TOKEN(401, "지원되지 않는 토큰입니다.", 401005),

    // 403 FORBIDDEN
    NOT_STORE_BOSS(403,"이 매장의 사장이 아닙니다.",403001),
    NOT_STORE_STAFF(403,"이 매장의 알바생이 아닙니다.",403002),
    SCHEDULE_NOT_BELONG_TO_STAFF(403, "스케줄이 해당 알바생의 것이 아닙니다.", 403003),
    ATTENDANCE_NOT_BELONG_TO_STAFF(403, "근태 기록이 해당 알바생의 것이 아닙니다.", 403004),

    // 404 Not Found
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다.", 404001),
    KAKAO_USER_INFO_NOT_FOUND(404, "카카오 사용자 정보 없음", 404002),
    INVITE_CODE_NOT_FOUND(404, "초대코드가 잘못되었습니다.", 404003),
    STAFF_NOT_FOUND(404,"해당하는 알바생이 없습니다.",404004),
    STORE_NOT_FOUND(404,"해당하는 매장이 없습니다.",404005),
    SCHEDULE_NOT_FOUND(404, "해당하는 스케줄이 없습니다.", 404006),
    NOT_YET_CLOCKED_IN(404, "아직 출근하지 않았습니다.", 404007),
    ATTENDANCE_NOT_FOUND(404, "근태 정보가 없습니다.", 404008),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(405, "HTTP 메서드가 잘못되었습니다.", 405001),

    // 409 CONFLICT
    ALREADY_SIGNED_UP(409, "이미 가입된 사용자입니다.", 409001),
    DUPLICATE_BUSINESS_NUMBER(409, "이미 망고보스에 등록된 사업자등록번호입니다.", 409002),
    ALREADY_JOIN_STAFF(409,"이미 매장에 가입한 알바생입니다.",409003),
    ALREADY_CLOCKED_IN(409, "이미 출근 처리된 스케줄입니다.", 409003),
    ALREADY_CLOCKED_OUT(409, "이미 퇴근 처리된 스케줄입니다.", 409004),

    // 500 INTERNAL_SERVER_ERROR
    BUSINESS_API_FAILED(500, "사업자 진위 확인에 실패했습니다.", 500001);

    private final int statusCode;
    private final String message;
    private final int detailStatusCode;

    CustomErrorInfo(final int statusCode, final String message, final int detailStatusCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.detailStatusCode = detailStatusCode;
    }
}
