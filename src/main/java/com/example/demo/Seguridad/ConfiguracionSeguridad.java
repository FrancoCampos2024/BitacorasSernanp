package com.example.demo.Seguridad;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/login", "/IMG/**").permitAll()
                        .requestMatchers("/Bitacoras/**", "/Unidades/**").hasAnyRole("ADMIN", "GUARDAPARQUE")
                        .requestMatchers("/Bienvenido").hasAnyRole("ADMIN", "GUARDAPARQUE")
                        .requestMatchers("/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            new org.springframework.security.web.savedrequest.HttpSessionRequestCache().removeRequest(request, response);
                            var roles = authentication.getAuthorities().toString();
                            if (roles.contains("ROLE_ADMIN")) {
                                response.sendRedirect("/Bienvenido");
                            } else if (roles.contains("ROLE_GUARDAPARQUE")) {
                                response.sendRedirect("/Unidades/ListaUnidades");
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .maximumSessions(-1)
                        .maxSessionsPreventsLogin(false)
                )
                .headers(header -> header
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
