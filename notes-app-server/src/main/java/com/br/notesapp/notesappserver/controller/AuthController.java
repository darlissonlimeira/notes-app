package com.br.notesapp.notesappserver.controller;

import com.br.notesapp.notesappserver.dto.ResponseDTO;
import com.br.notesapp.notesappserver.dto.auth.AuthTokens;
import com.br.notesapp.notesappserver.dto.auth.LoginRequestDTO;
import com.br.notesapp.notesappserver.service.AuthService;
import com.br.notesapp.notesappserver.service.JWTCookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final JWTCookieService cookieService;

    public AuthController(AuthService authService, JWTCookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        System.out.println(request.username());
        Optional<AuthTokens> tokens = authService.login(request);
        if (tokens.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        cookieService.setJWTCookie(tokens.get().refreshToken(), response);
        var loginResponse = new ResponseDTO();
        loginResponse.put("access_token", tokens.get().accessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        cookieService.removeJWTCookie(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResponseDTO> refresh(HttpServletRequest request) {
        var token = cookieService.getJWT(request);
        if (token.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var accessToken = authService.getAccessToken(token.get());
        if (accessToken.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var tokenResponse = new ResponseDTO();
        tokenResponse.put("access_token", accessToken.get());
        return ResponseEntity.ok(tokenResponse);
    }
}
