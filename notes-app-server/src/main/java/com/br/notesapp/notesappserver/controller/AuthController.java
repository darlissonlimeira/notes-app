package com.br.notesapp.notesappserver.controller;

import com.br.notesapp.notesappserver.dto.ResponseDTO;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import com.br.notesapp.notesappserver.service.AuthService;
import com.br.notesapp.notesappserver.utils.JwtCookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        var loginResponse = new ResponseDTO();
        var tokens = authService.login(request);
        var jwtCookie = JwtCookieUtils.createJwtCookie(tokens.refreshToken());
        loginResponse.put("access_token", tokens.accessToken());
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        var resetCookie = JwtCookieUtils.createResetCookie();
        response.addCookie(resetCookie);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResponseDTO> refresh(HttpServletRequest request) {
        var tokenResponse = new ResponseDTO();
        var accessToken = authService.getAccessToken(request);
        tokenResponse.put("access_token", accessToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
