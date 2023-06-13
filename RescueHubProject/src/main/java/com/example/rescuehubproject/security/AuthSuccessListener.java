package com.example.rescuehubproject.security;

import com.example.rescuehubproject.accounts.entity.Log;
import com.example.rescuehubproject.accounts.util.LogEvent;
import com.example.rescuehubproject.accounts.services.LogService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Configuration
@AllArgsConstructor
public class AuthSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LogService logService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();

//        troche bez sensu logowac poprawna autoryzacje ale jak checie to mozna
        //tu powinno sie zerowac licznik niepopranwych prob logowania ale blokowanie nie dziala wiec nie robie heh

//        if (username != null) {
//            Log log = Log.builder()
//                    .setAction(LogEvent.AUTH_SUCCESS)
//                    .setSubject(username)
//                    .setObject(username)
//                    .setPath("/api/auth/login")
//                    .build();
//            logService.saveLog(log);
//        }

    }
}
