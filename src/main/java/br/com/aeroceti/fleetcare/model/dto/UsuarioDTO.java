/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * RECORD para tratar objetos de Usuarios transitorios.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Schema(name = "B. UsuarioDTO")
public record UsuarioDTO(
        @Schema(example = "01", description = "ID do Usuario na Base de Dados")
        Long usuarioID,
        
        @Schema(example = "55566677700", description = "CPF Valido do Usuario")
        String cpf,
        
        @Schema(example = "Fulano", description = "Nome do Usuario")
        String nome,
        
        @Schema(example = " de Tal", description = "Sobrenome do Usuario")
        String sobrenome,
        
        @NotBlank(message = "{roles.email.notblank}")  @Email(message = "{roles.email.notvalid}")
        @Pattern(
            regexp = "^[^@]+@[^@]+(\\.[a-zA-Z]{2,})+$",
            message = "O e-mail deve possuir domínio válido (ex.: nome@dominio.com)"
        )                
        @Schema(example = "fulano@email.com", description = "E-mail do usuario")
        String email,
        
        @Schema(example = "10/12/2000", description = "Data de nascimento do usuario")
        String dataNascimento,
        
        @Schema(example = "31988887777", description = "Telefone com Whatsapp do Usuario")
        String whatsapp,
        
        @Schema(example = "M", description = "Sexo do Usuario (M=masculino e F=fenimino")
        String sexo,
        
        @Schema(example = "MinhaSenha2025", description = "Senha do usuario, contendo no minimo 8 caracteres")
        String password
        ) {

}
/*                    End of Class                                            */
