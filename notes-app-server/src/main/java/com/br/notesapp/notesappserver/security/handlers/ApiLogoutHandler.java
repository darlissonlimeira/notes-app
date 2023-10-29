package com.br.notesapp.notesappserver.security.handlers;

import com.br.notesapp.notesappserver.security.jwt.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

@Service
public class ApiLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (!request.getMethod().equals("GET")) {
            throw new RequestRejectedException(String.format("%s request method is not allowed", request.getMethod()));
        }
        AuthResponse authResponse = new AuthResponse(response);
        authResponse.clearRefreshToken();
    }
}
