/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.components;

import br.com.aeroceti.fleetcare.model.user.Usuario;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Componente para realizar a validacao de senha no formulario de cadastro.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public class SenhasIguaisValidator implements ConstraintValidator<SenhasIguais, Usuario> {

    public SenhasIguaisValidator() {
    }
    
    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        // evita erro em formulários parciais
        if (usuario.getPassword() == null || usuario.getConfirmarSenha() == null) {
            return true; 
        }

        boolean iguais = usuario.getPassword().equals(usuario.getConfirmarSenha());
        if (!iguais) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("confirmarSenha")
                .addConstraintViolation();
        }
        return iguais;
    }
    
}
/*                    End of Class                                            */