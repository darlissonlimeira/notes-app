package com.br.notesapp.notesappserver.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class JWTCookieService {

    private final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60 * 1000;

    private final String COOKIE_PATH = "/";

    private final String COOKIE_NAME = "jwt";

    public void setJWTCookie(String token, HttpServletResponse response) {
        var jwtCookie = createJWTCookie(token, COOKIE_MAX_AGE);
        response.addCookie(jwtCookie);
    }

    public void removeJWTCookie(HttpServletResponse response) {
        var jwtCookie = createJWTCookie("", 0);
        response.addCookie(jwtCookie);
    }

    public Optional<String> getJWT(HttpServletRequest request) {
        var jwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(COOKIE_NAME)).findFirst();
        return jwtCookie.map(Cookie::getValue);
    }

    private Cookie createJWTCookie(String value, int maxAge) {
        var jwtCookie = new Cookie(COOKIE_NAME, value);
        jwtCookie.setPath(COOKIE_PATH);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setAttribute("SameSite", "None");
        jwtCookie.setMaxAge(maxAge);
        return jwtCookie;
    }


}
