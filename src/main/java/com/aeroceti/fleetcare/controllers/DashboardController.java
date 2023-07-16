/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * REST Controller para a inicializacao do sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping
public class DashboardController {

    Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @GetMapping("/")
    public String getHomePage(/*Authentication authentication, */ Model model){
        model.addAttribute("name", "Murilo");
        logger.info("Redirecionando view para Pagina Inicial...");
        return "index";        
    }   
    
    @GetMapping("/login")
    public String getLoginPage(){
        logger.info("Redirecionando view para pagina de Login...");
        return "login";
    }

    @GetMapping("/dashboard")
    public String getDashboard(){
        logger.info("Redirecionando view para Dashboard...");
        return "dashboard";
    }

    @GetMapping("/403erro")
    public String getErrorPage403(){
        logger.info("Redirecionando view para Pagina de Erro 403...");
        return "Error403";
    }
       
}
