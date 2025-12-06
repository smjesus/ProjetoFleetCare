/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.repositories;

import java.util.List;
import java.util.Optional;
import br.com.aeroceti.fleetcare.model.carro.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface para o Repositorio de Modelo.
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    // obtem uma lista de Permissoes ORDENADA por nome
    List<Modelo> findByOrderByModeloNameAsc();

    // obtem uma Permissao atraves do nome
    Optional<Modelo> findByModeloName(String chavePesquisa);

    // obtem uma Permissao atraves do ID
    Optional<Modelo> findByModeloID(Long chavePesquisa);

    // obtem o numero de Permissoes por ID
    int countByModeloID(Long rulesID);
    
    // obtem o numero de Permissoes por nome
    int countByModeloName(String ruleName);    

}
/*                    End of Class                                            */