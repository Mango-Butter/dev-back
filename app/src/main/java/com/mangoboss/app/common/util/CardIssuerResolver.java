package com.mangoboss.app.common.util;

public class CardIssuerResolver {

    public static String resolveIssuerName(String issuerCode) {
        return switch (issuerCode) {
            case "3" -> "BC카드";
            case "4" -> "국민카드";
            case "6" -> "삼성카드";
            case "11" -> "현대카드";
            case "12" -> "롯데카드";
            case "13" -> "하나카드";
            case "14" -> "우리카드";
            case "21" -> "신한카드";
            default -> "알 수 없음";
        };
    }
}
