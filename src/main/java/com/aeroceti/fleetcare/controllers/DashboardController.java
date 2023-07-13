/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller para a inicializacao do sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
public class DashboardController {

    Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @GetMapping("/")
    public String sig_in(){
        logger.info("Redirecionando view para login...");
        return "index";
    }

    @GetMapping("/dashboard")
    public String index(){
        logger.info("Redirecionando view para Dashboard...");
        return "dashboard";
    }
       
}
