/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.repositories;

import java.util.UUID;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.aeroceti.fleetcare.model.user.UsuarioVerificador;

/**
 * Interface para o Repositorio dos Codigos de Verificacao de Contas (UsuarioVerificador).
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface UsuarioVerificadorRepository extends JpaRepository<UsuarioVerificador, Long> {

    // obtem uma Permissao atraves do UUID
    Optional<UsuarioVerificador> findBycodigoUUID(UUID chavePesquisa);

    @Query("SELECT v FROM UsuarioVerificador v WHERE v.usuario.entidadeID = :usuarioId")
    Optional<UsuarioVerificador> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
}
/*                    End of Class                                            */