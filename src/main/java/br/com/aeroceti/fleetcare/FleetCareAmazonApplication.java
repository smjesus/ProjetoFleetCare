/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Classe principal da Aplicacao usando o Spring Boot.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@SpringBootApplication()
public class FleetCareAmazonApplication  extends SpringBootServletInitializer {

        public static void main(String[] args) {
		SpringApplication.run(FleetCareAmazonApplication.class, args);
	}

}
/*                    End of Class                                            */