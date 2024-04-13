package com.br.notesapp.notesappserver.utils;

import jakarta.servlet.http.Cookie;

public class JwtCookieUtils {

    public static Cookie createJwtCookie(String token) {
        var jwtCookie = new Cookie("jwt", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setAttribute("SameSite", "None");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60 * 1000);
        return jwtCookie;
    }

    public static Cookie createResetCookie() {
        var resetCookie = new Cookie("jwt", "");
        resetCookie.setPath("/");
        resetCookie.setHttpOnly(true);
        resetCookie.setAttribute("SameSite", "None");
        resetCookie.setMaxAge(0);
        return resetCookie;
    }
}
