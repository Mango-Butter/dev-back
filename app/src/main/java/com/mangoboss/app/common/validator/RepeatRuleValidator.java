package com.mangoboss.app.common.validator;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.task.request.RepeatRule;

public class RepeatRuleValidator {

    public static void validateMonthlyRepeatDates(final RepeatRule repeatRule) {
        if (repeatRule == null || repeatRule.repeatDates() == null || repeatRule.repeatDates().isEmpty()) {
            throw new CustomException(CustomErrorInfo.INVALID_REPEAT_DATES);
        }

        if (repeatRule.repeatDays() != null && !repeatRule.repeatDays().isEmpty()) {
            throw new CustomException(CustomErrorInfo.UNSUPPORTED_REPEAT_DAYS_FOR_MONTHLY);
        }

        for (Integer date : repeatRule.repeatDates()) {
            if (date < 1 || date > 31) {
                throw new CustomException(CustomErrorInfo.INVALID_REPEAT_DATE_RANGE);
            }
        }
    }

    public static void validateWeeklyRepeatDays(final RepeatRule repeatRule) {
        if (repeatRule == null || repeatRule.repeatDays() == null || repeatRule.repeatDays().isEmpty()) {
            throw new CustomException(CustomErrorInfo.INVALID_REPEAT_DAYS);
        }

        if (repeatRule.repeatDates() != null && !repeatRule.repeatDates().isEmpty()) {
            throw new CustomException(CustomErrorInfo.UNSUPPORTED_REPEAT_DATES_FOR_WEEKLY);
        }
    }

    public static void validateNoRepeatRuleForDaily(final RepeatRule repeatRule) {
        if (repeatRule != null) {
            boolean hasRepeatDays = repeatRule.repeatDays() != null && !repeatRule.repeatDays().isEmpty();
            boolean hasRepeatDates = repeatRule.repeatDates() != null && !repeatRule.repeatDates().isEmpty();

            if (hasRepeatDays || hasRepeatDates) {
                throw new CustomException(CustomErrorInfo.UNSUPPORTED_REPEAT_RULE_FOR_DAILY);
            }
        }
    }
}