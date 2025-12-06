/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RECORD para encapsular os dados de um email.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Schema(name = "D. EmailMessageDTO") 
public record EmailMessageDTO(
        @Schema(example = "fleetcare@aeroceti.com", description = "E-mail do destinatário")
        String to, 
        
        @Schema(example = "fulano@email.com.br",    description = "E-mail do remetente")
        String from, 
        
        @Schema(example = "Fulano de Tal",          description = "Nome do Usuário que originou a mensagem")
        String nomeUsuario, 
        
        @Schema(example = "Cadastro no FleetCARE",  description = "Texto contendo o assunto")
        String subject, 
        
        @Schema(example = "Mensagem automatica de cadastro, seja bem vindo!",        description = "Texto (ou HTML) com o conteúdo")
        String textBody) {
    
}
/*                    End of Class                                            */