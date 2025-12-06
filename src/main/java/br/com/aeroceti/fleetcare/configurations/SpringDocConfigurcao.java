/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  CONFIGURACAO para o Spring DOC - Documentacao da API.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */

@Configuration
public class SpringDocConfigurcao {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fleet Care Amazon API")
                        .version("v1 - 0.2")
                        .description("REST API Fleet Care - Sistema de Controle de Locacao e Manutencao de Veiculos.<BR>" +
                                     "A Versão 0.1 foi criada em Fevereiro de 2024 para prover recursos funcionais para complementar o Sistema FleetCare.<BR>" +
                                     "A Versão 0.2 foi melhorada e testada em Novembro de 2025."
                        )
                        
                        .license(new License()
                                .name("Apache Licence 2.0")
                                .url("https://opensource.org/license/apache-2-0/")
                        )
                        
                );
    }
    
}
/*                    End of Class                                            */