package com.dju.woojiman.global.auth.dto;

public record TokenResult(String accessToken, String refreshToken, String linkToken, boolean profileCompleted) {
}
