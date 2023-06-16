package com.example.rescuehubproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String ADMIN = "ADMINISTRATOR";
    private static final String USER = "USER";
    private static final String ADOPTER = "ADOPTER";

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http
                .httpBasic()
                .authenticationEntryPoint(getAuthenticationEntryPoint())
                .and()
                .formLogin().permitAll()
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/changepass/**").hasAnyRole(ADMIN, USER, ADOPTER)
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/admin/logs").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/empl/authenticated").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/empl/protected").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/auth/adopter").hasRole(ADOPTER)

                        //*****ANIMAL SPECIES*****\\
                        .requestMatchers(HttpMethod.GET, "/api/animalspecies/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animalspecies").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/animalspecies").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/animalspecies/{id}").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/animalspecies/{id}").hasRole(ADMIN)
                        //*****ANIMAL SPECIES*****\\

                        //*****ANIMAL *****\\
                        .requestMatchers(HttpMethod.GET, "/api/animalspecies/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animalspecies").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/animalspecies").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/animalspecies/{id}").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/animalspecies/{id}").hasRole(ADMIN)
                        //*****ANIMAL *****\\

                        //*****ADOPTER*****\\
                        .requestMatchers(HttpMethod.GET, "/api/adopter").hasAnyRole(ADMIN, USER)
                        .requestMatchers(HttpMethod.GET, "/api/adopter/{id}").hasAnyRole(ADMIN, USER)
                        .anyRequest().permitAll()



                )
                .userDetailsService(userDetailsService)
                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler())
                .and()
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
