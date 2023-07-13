/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.dataTransfers;

/**
 *  RECORD para apresentar mensagens de erro.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public record ExceptionsDTO (String message, String typeError ) {   
}
