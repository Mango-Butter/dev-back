package com.mangoboss.app.dto;

import java.util.List;

public record ListWrapperResponse<T>(
        List<T> result
) {
    public static <T> ListWrapperResponse<T> of(List<T> result) {
        return new ListWrapperResponse<>(result);
    }
}