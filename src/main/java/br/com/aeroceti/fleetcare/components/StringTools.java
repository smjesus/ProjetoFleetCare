/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.components;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Este Componente fornece funcoes para tratamento de Strings.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public class StringTools {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /*
     * Esta funcao gera uma String Aleatoria para servir de Codigo de Verificacao
     */
    public static String generateRandomString(int length){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++){
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    /*
     * Esta formata um nome com as iniciais maiusculas e demais minusculas
     */
    public static String formatName(String nome) {
        try {
            String[] partes = nome.split(" ");
            StringBuilder sb = new StringBuilder();
            String [] codes = {"da", "de", "do", "di", "dos", "das", "e", "d'"};
            for (int i = 0; i < partes.length; i++) {
                String parte = partes[i];
                if( !parte.trim().equals("") ) {
                    if( i == 0 ) {
                        parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                    } else  {
                        if(Arrays.asList(codes).contains( parte.toLowerCase().trim()) ) {
                            parte = parte.toLowerCase();
                        }  else  {
                            parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                        }
                    }
                    sb.append(" ").append(parte.trim());
                }
            }
            nome = sb.toString().trim();
        } catch(Exception nameError) {
            nome = "" ;
        }
        return nome;
    }
    
}
/*                    End of Class                                            */