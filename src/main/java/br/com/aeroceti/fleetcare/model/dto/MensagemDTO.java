/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RECORD para apresentar mensagens para as requisicoes REST.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Schema(name = "C. MensagemDTO", description = "Objeto do tipo RECORD para serialização de uma mensagem na resposta para a requisição na API") 
public record MensagemDTO (
        @Schema(example = "Mensagem apresentada na resposta", description = "String contendo a resposta para a requisicao")
        String mensagem
        ){
}
/*                    End of Class                                            */