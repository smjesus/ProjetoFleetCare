/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.aeroceti.fleetcare.model.dto.MensagemDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Classe Controller Advice para manupular as excessões no sistema.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@ControllerAdvice
public class ManupuladorExceptions extends RuntimeException {
    
    private final Logger logger = LoggerFactory.getLogger(ManupuladorExceptions.class);
    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> generalExceptions(Exception ex) {
//        logger.info("Exception Gerada: " + ex.getMessage() + " Retornando BAD Request!");
//        return new ResponseEntity<>(new MensagemDTO( ex.getMessage() ), HttpStatus.BAD_REQUEST);
//    }
    
    @ExceptionHandler(AuthenticationException.class)
    public void falhaAutenticacao(AuthenticationException error, HttpHeaders header) {
        logger.info("Exception Gerada: " + error.getMessage() + " - Tentativa de LOGIN com usuário não cadastrado!");
        logger.info("Host ({}) e Origem: {}", header.getHost().toString(), header.getOrigin());
    }
}
/*                    End of Class                                            */