/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.components;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * ANOTACAO para a confirmacao de senha no cadastro
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = SenhasIguaisValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhasIguais {
    String message() default "{form.user.validar.senha}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
/*                    End of Class                                            */