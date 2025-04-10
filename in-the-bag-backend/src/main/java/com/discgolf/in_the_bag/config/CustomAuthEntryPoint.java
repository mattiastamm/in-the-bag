package com.discgolf.in_the_bag.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        final Exception jwtException = (Exception) request.getAttribute("jwtException");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        if (jwtException instanceof ExpiredJwtException) {
            logger.warn("🔒 Token has expired.");
            response.getWriter().write("""
                {
                    "error": "TokenExpired",
                    "message": "Your session has expired. Please log in again."
                }
            """);
        } else {
            logger.warn("🔒 Unauthorized access or invalid token.");
            response.getWriter().write("""
                {
                    "error": "Unauthorized",
                    "message": "You are not authorized to access this resource."
                }
            """);
        }
    }
}


