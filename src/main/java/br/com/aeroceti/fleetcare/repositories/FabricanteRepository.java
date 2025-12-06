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
import br.com.aeroceti.fleetcare.model.carro.Fabricante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Interface para o Repositorio de Fabricante.
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface FabricanteRepository extends JpaRepository<Fabricante, Long> {

    // obtem o numero de Permissoes por ID
    int countByEntidadeID(Long rulesID);
    
    // obtem o numero de Permissoes por nome
    int countByFabricanteName(String ruleName);   
    
    // obtem uma lista de Permissoes ORDENADA por nome
    List<Fabricante> findByOrderByFabricanteNameAsc();

    // obtem uma Permissao atraves do nome
    Optional<Fabricante> findByFabricanteName(String chavePesquisa);

    // obtem uma Permissao atraves do ID
    Optional<Fabricante> findByEntidadeID(Long chavePesquisa);
    
    // obtem uma lista de Colaboradores com PAGINACAO
    @Query(value = "select * from Fabricante order by FabricanteName ASC", nativeQuery = true )
    Page<Fabricante> findAllFabricantes(Pageable page);
    
       // obtem uma lista de Permissoes ORDENADA por nome
    Page<Fabricante> findByOrderByFabricanteNameAsc(Pageable page); 

}
/*                    End of Class                                            */