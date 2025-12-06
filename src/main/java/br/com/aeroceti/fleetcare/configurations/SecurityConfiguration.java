/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import br.com.aeroceti.fleetcare.components.CustomAuthenticationFailureHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Esta classe realiza a configuracao da Seguranca do Spring.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    
    private final CustomAuthenticationFailureHandler failureHandler;    
    
    public SecurityConfiguration(CustomAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/termos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/novaconta").permitAll()
                        .requestMatchers(HttpMethod.GET, "/privacidade").permitAll()
                        .requestMatchers(HttpMethod.GET, "/documentacao").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuario/uuid/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/novaconta").permitAll()
                        .requestMatchers("/content-scripts/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/assets/**").permitAll()
                        .anyRequest().authenticated()
                        

                )
                
                .oauth2Login( oac -> oac.loginPage("/login") )
                
                .formLogin( (formulario) -> formulario
                        .loginPage("/login")
                        .failureHandler(failureHandler)
                        .defaultSuccessUrl("/dashboard")
                        .permitAll() 
                )
                
                .rememberMe(remember -> remember
                        .key("key-Remember-Me-OnFleetCARE")
                        .tokenValiditySeconds(5 * 24 * 60 * 60) // 5 dias
                )
        
                .logout( (logout) -> logout
                        .logoutUrl("/logout").logoutSuccessUrl("/").permitAll() )
                
                //.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()) )
                //.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
/*                    End of Class                                            */