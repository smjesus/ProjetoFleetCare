/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.dataTransfers;

import org.springframework.stereotype.Component;

/**
 * Classe para apresentar mensagens para as requisicoes REST.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Component
public class MensagemDTO {

    private String mensagem;

    public MensagemDTO() {
    }

    public MensagemDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
