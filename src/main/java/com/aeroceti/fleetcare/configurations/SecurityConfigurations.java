/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.configurations;

import com.aeroceti.fleetcare.services.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuracao do Security Sprint.
 *
 * Esta classe reconfigura o comportamento padrao do Spring Security.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain filterChainAPI(HttpSecurity http) throws Exception {
        // reconfigura o comportamento padrao de seguranca:
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/", "/login", "/logout", "/dashboard",
                        "/api/v1/usuario/email/**", "/api/v1/usuario/senha/**"
                ).permitAll()
                .anyRequest().permitAll() //.authenticated() 

                ).csrf(t -> {
                    t.disable();
                })
                
                .cors(cors -> {
                    cors.disable();
                })
                //.formLogin(Customizer.withDefaults())
                .logout(logout
                        -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                        .logoutSuccessUrl("/login")
                )
                .exceptionHandling(ehc -> ehc.accessDeniedHandler(accessDeniedHandler()));

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
