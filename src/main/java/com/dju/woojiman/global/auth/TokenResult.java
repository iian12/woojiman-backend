package com.dju.woojiman.global.auth;

public record TokenResult(String accessToken, String refreshToken, String linkToken, boolean profileCompleted) {
}
