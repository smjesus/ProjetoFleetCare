/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 *  RECORD para tratar objetos de Usuarios transitorios.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Schema(name = "A. NivelAcessoDTO") 
public record NivelAcessoDTO  (
        @Schema(example = "0", description = "ID na Base de dados do objeto")
        Long entidadeID, 
        
        @NotBlank
        @Schema(example = "Gerente", description = "Nome do Nivel de Acesso para os úsuarios")
        String nome ) {
}
/*                    End of Class                                            */