/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.repositories;

import java.util.UUID;
import com.aeroceti.fleetcare.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  Interface para o Repositorio de Usuarios.
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, UUID> {
    @Override
    //Listagem completa dos usuarios cadastrados
    List<Usuario> findAll();
    
    // obtem um usuario atraves do email
    Usuario findByEmail(String chavePesquisa);
    
    // obtem um usuario atraves do ID
    Usuario findByUsuarioID(UUID chavePesquisa);
        
    // obtem uma lista de usuarios ORDENADA por nome
    List<Usuario> findByOrderByNomeAsc();

    // obtem uma lista de usuarios ORDENADA por email
    List<Usuario> findByOrderByEmailAsc();

    // obtem o numero de usuarios por email
    int countByEmail(String email);
    
    // obtem o numero de usuarios por ID
    int countByUsuarioID(UUID usuarioID);
    

}
