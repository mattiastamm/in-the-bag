package com.discgolf.in_the_bag.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        final Exception jwtException = (Exception) request.getAttribute("jwtException");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        if (jwtException instanceof ExpiredJwtException) {
            response.getWriter().write("""
            {
                "error": "TokenExpired",
                "message": "Your session has expired. Please log in again."
            }
        """);
        } else {
            response.getWriter().write("""
            {
                "error": "Unauthorized",
                "message": "You are not authorized to access this resource."
            }
        """);
        }
    }
}


