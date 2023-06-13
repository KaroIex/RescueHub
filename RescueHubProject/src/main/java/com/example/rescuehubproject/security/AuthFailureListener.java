package com.example.rescuehubproject.security;

import com.example.rescuehubproject.accounts.entity.Log;
import com.example.rescuehubproject.accounts.util.LogEvent;
import com.example.rescuehubproject.accounts.services.LogService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@Configuration
@AllArgsConstructor
public class AuthFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LogService logService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();

        Log log = Log.builder()
                .setAction(LogEvent.LOGIN_FAILED)
                .setSubject(username)
                .setObject(username)
                .setPath("/api/auth/login")
                .build();
        logService.saveLog(log);

    }
}
