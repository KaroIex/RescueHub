package com.example.rescuehubproject.security;

import com.example.rescuehubproject.accounts.entity.Log;
import com.example.rescuehubproject.accounts.services.LogService;
import com.example.rescuehubproject.accounts.util.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private LogService logService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        String subject = SecurityContextHolder.getContext().getAuthentication().getName();

        Log log = Log.builder()
                .setAction(LogEvent.ACCESS_DENIED)
                .setSubject(subject)
                .setObject(requestUri)
                .setPath(requestUri)
                .build();
        logService.saveLog(log);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getOutputStream().println(new ObjectMapper().writeValueAsString(Map.of(
                "timestamp", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden",
                "message", "Access Denied!",
                "path", request.getRequestURI()
        )));
    }

}
