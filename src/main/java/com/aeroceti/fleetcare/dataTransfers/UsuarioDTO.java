/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.dataTransfers;

import jakarta.validation.constraints.NotBlank;

/**
 *  RECORD para tratar objetos de Usuarios transitorios.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public record UsuarioDTO (@NotBlank String email,@NotBlank String cpf, @NotBlank String password ) {
    
}
