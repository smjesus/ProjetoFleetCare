/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface para o Repositorio de Usuarios.
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // obtem o numero de usuarios por email
    int countByEmail(String email);

    // obtem o numero de usuarios por ID
    int countByEntidadeID(Long usuarioID);

    // obtem um usuario atraves do cpf
    Optional<Usuario> findByCpf(String chavePesquisa);

    // obtem um usuario atraves do email
    Optional<Usuario> findByEmail(String chavePesquisa);
    
    // obtem uma lista de usuarios ORDENADA por nome
    List<Usuario> findByOrderByNomeAsc();

    // obtem uma lista de Colaboradores com PAGINACAO
    Page<Usuario> findByOrderByNomeAsc(Pageable page);

    // obtem uma lista de usuarios ORDENADA por email
    List<Usuario> findByOrderByEmailAsc();

    // obtem um usuario atraves do ID
    Optional<Usuario> findByEntidadeID(Long chavePesquisa);

    // 🔍 Busca todos os colaboradores com seus Niveis de Acesso
    @Query("""
           SELECT DISTINCT c FROM Usuario c
           LEFT JOIN FETCH c.nivelAcesso
           """)
    List<Usuario> findAllComRelacionamentos();
    
    // 🔍 Busca um colaborador específico trazendo NivelAcesso
    @Query("""
           SELECT c FROM Usuario c
           LEFT JOIN FETCH c.nivelAcesso
           WHERE c.entidadeID = :id
           """)
    Optional<Usuario> findByIdComRelacionamentos(@Param("id") Long id);

}
/*                    End of Class                                            */