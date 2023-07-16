/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Adviser para o Handler de Acesso Negado
 *
 * Esta classe reconfigura o comportamento erro Acesso Negado.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestControllerAdvice
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI());
        }
        logger.info("Attempted to access the protected URL: " + request.getRequestURI() + " (" + accessDeniedException.getMessage() + ")");
        response.sendRedirect(request.getContextPath() + "/403erro");
    }

}
