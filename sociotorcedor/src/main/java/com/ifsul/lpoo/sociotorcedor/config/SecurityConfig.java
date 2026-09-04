package com.ifsul.lpoo.sociotorcedor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/", "/login").permitAll()
                .requestMatchers("/noticias", "/noticias/**").permitAll()
                .requestMatchers("/jogos", "/jogos/calendario").permitAll()
                .requestMatchers("/loja", "/loja/**").permitAll()

                .requestMatchers("/associados/tornar-socio").authenticated()
                .requestMatchers("/loja/resgatar/**").authenticated()
                .requestMatchers("/usuario/**").hasAnyRole("ADMIN", "USER")

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                            .loginPage("/login")
                            .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}
