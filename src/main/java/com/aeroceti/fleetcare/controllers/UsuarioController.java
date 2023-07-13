/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.controllers;

import com.aeroceti.fleetcare.model.Usuario;
import com.aeroceti.fleetcare.repositories.UsuarioRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Classe REST Controller para o objeto Usuario.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
public class UsuarioController {
    @Autowired
    UsuarioRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    
    /**
     * Verifica se um email esta cadastrado no banco de dados.
     * @param email - endereco que se deseja verificar
     * @return Boolean - Verdadeiro se existe ou Falso se nao existe
     */
    @GetMapping("/verifica_email/{email}")
    public boolean checkLogin (@PathVariable String email) {
        // verifica se existe um usuario com o e-mail informado.
        logger.info("Verificando se o e-mail (" + email + ") está cadastrado...");
        Usuario user = userRepository.findByEmail(email);
        boolean situacao = false;
        try {
              if( user.getEmail().toLowerCase().trim().equals(email.toLowerCase().trim()) )
                  situacao = true;
        } catch(NullPointerException npe) {
              logger.info("Verificacao Falhou! Objeto NULO detectado!!");
        }
        return situacao;
    }
    
    /**
     * Busca todos os dados de um Usuario a partir do seu e-mail
     * @param email endereco de email do usuario
     * @return USUARIO - Ojbeto JSON contendo todos os dados cadastrados
     */
    @GetMapping("buscar/{email}")
    public Usuario buscarPeloEmail(@PathVariable String email) {
        // retorna UM usuario com o email informado.
        logger.info("Buscando no banco um usuario com o email: " + email);        
        return userRepository.findByEmail(email);
    }
        
    @PostMapping("/usuario")
    public Usuario usuario(@RequestBody Usuario user) {
        // retorna UM usuario.
        logger.info("Buscando no banco um usuario...");
        return user;
    }
    
    /**
     * Listagem de TODOS os usuarios cadastrados no Banco de dados.
     * @return ArrayList em JSON com varios objetos USUARIO
     */
    @GetMapping("/todos_usuarios")
    public List<Usuario> listagem() {
        // retorna todos os usuarios cadastrados.
        logger.info("Obtendo uma listagem de todos os usuarios...");
        return userRepository.findAll();
    }

    @GetMapping("/todos_usuarios_asc")
    public List<Usuario> listagemAsc() {
        // retorna todos os usuarios cadastrados.
        logger.info("Obtendo uma listagem de todos os usuarios...");
        return userRepository.findByOrderByNomeAsc();
    }
    
   @PutMapping("/persistir")
   public Usuario persistirUsuario (@RequestBody Usuario user) {
        // altualiza ou cria um usuario no banco de dados (PERSISTTE)
        logger.info("Persistindo um usuario(" + user.toString() + ") no banco...");       
        return userRepository.save(user);
   }
   
   @DeleteMapping("/arquivar/{email}")
   public boolean deleteUsuario (@PathVariable String email) {
        boolean situacao = false;
        // buscando um usuario com o e-mail informado...
        logger.info("Buscando o usuario pelo e-mail (" + email + ") ...");
        Usuario user = userRepository.findByEmail(email);
        // Se encontrar, entao desativa o usuario:
        try {
              if( user.getEmail().toLowerCase().trim().equals(email.toLowerCase().trim()) ) {
                  user.setAtivo(false);
                  situacao = true;
                  userRepository.save(user);
                  logger.info("Usuario desativado no sistema!");
              }
        } catch(NullPointerException npe) {
              logger.info("Verificacao Falhou! Objeto NULO detectado!!");
        }
        return situacao;
   }

}
