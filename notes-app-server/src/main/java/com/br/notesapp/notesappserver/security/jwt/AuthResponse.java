package com.br.notesapp.notesappserver.security.jwt;

import com.br.notesapp.notesappserver.utils.CustomBody;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class AuthResponse {

    private final HttpServletResponse response;
    private final Gson gson = new Gson();

    private final CustomBody body = new CustomBody();


    public AuthResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void addJWTCookie(String refreshToken) {
        Cookie cookie = new Cookie("jwt", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge(5 * 24 * 60 * 60 * 1000);
        response.addCookie(cookie);

    }

    public void addJWTReponseBody(String accessToken) throws IOException {
        body.addProperty("accessToken", accessToken);
        String jsonBody = gson.toJson((Object) body);
        response.getWriter().print(jsonBody);
    }

    public void successReponse() throws IOException {
        configureResponse();
        response.setStatus(200);
        response.getWriter().close();
    }

    public void failReponse(String message) throws IOException {
        configureResponse();
        body.addProperty("message", message);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.getWriter().print(gson.toJsonTree(body));
        response.getWriter().close();
    }

    private void configureResponse() {
        response.addHeader("Content-Type", "application/json");
        response.setCharacterEncoding("UTF-8");
    }


    public void clearRefreshToken() {
        try {
            addJWTCookie("");
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            response.setStatus(200);
        }
    }
}
