package com.dju.woojiman.global.auth.controller;

import com.dju.woojiman.global.auth.AuthService;
import com.dju.woojiman.global.auth.dto.IdTokenDto;
import com.dju.woojiman.global.auth.dto.TokenResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody IdTokenDto idTokenDto, HttpServletResponse response) {
        TokenResult tokenResult = authService.processingGoogleUser(idTokenDto);

        response.setHeader("Authorization", "Bearer " + tokenResult.accessToken());
        response.setHeader("Refresh-Token", tokenResult.refreshToken());

        return ResponseEntity.ok().build();
    }
}
