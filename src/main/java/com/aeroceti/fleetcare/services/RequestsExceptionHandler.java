/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.services;

import com.aeroceti.fleetcare.dataTransfers.ExceptionsDTO;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manipulador de Exceptions.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestControllerAdvice
public class RequestsExceptionHandler {
    
    Logger logger = LoggerFactory.getLogger(RequestsExceptionHandler.class);
    
    @ExceptionHandler(StaleObjectStateException.class)
    public ResponseEntity updateFail() {
        var mensagem = new ExceptionsDTO("Alteracao nao realizada: ESTADO incorreto do Objeto!", "StaleObjectStateException");
        logger.info("Alteracao nao realizada: ESTADO incorreto do Objeto!");
        return ResponseEntity.badRequest().body(mensagem);
    }
    
}
