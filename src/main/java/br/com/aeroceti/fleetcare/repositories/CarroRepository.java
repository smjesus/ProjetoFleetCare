/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.repositories;


import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.aeroceti.fleetcare.model.carro.Carro;

/**
 * Interface para o Repositorio de Carro.
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {

    // obtem uma lista de Permissoes ORDENADA por nome
    List<Carro> findByOrderByNumeroDaPlacaAsc();

    // obtem uma Permissao atraves do nome
    Optional<Carro> findByNumeroDaPlaca(String chavePesquisa);

    // obtem uma Permissao atraves do ID
    Optional<Carro> findByCarroID(Long chavePesquisa);

    // obtem o numero de Permissoes por ID
    int countByCarroID(Long rulesID);
    
    // obtem o numero de Permissoes por nome
    int countByNumeroDaPlaca(String placa);    

}
/*                    End of Class                                            */