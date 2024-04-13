package com.br.notesapp.notesappserver.config;

import com.br.notesapp.notesappserver.dto.auth.TokenPayload;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {

    String resolve(HttpServletRequest request);

    String generate(UserDetails userDetails);

    TokenPayload verify(String token);
}
