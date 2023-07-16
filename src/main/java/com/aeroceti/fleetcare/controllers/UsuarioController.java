/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.controllers;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import com.aeroceti.fleetcare.model.Usuario;
import com.aeroceti.fleetcare.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe REST Controller para o objeto Usuario.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/usuario/")
public class UsuarioController {

    @Autowired
    private final UsuarioService userService;

    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    public UsuarioController(UsuarioService usersvc) {
        this.userService = usersvc;
    }

    /**
     * Cria um Usuario na base de dados.
     *
     * Se a propriedade usuarioID nao estiver presente (nulla ou em branco) sera
     * criado um usuario; mas se a propriedade estiver presente, sera atualizado
     * o usuario com esse ID.
     *
     * Nao necessita ter todas as propriedades na requisicao.
     *
     * @param user - Objeto Usuario a ser persistido ou atualizado no banco
     * @return Objeto User, com todos as propriedades (em branco ou preenchidas)
     */
    @PutMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario user) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.cadastrar(user);
    }

    /**
     * Atualiza um Usuario na base de dados.
     *
     * Se a propriedade usuarioID nao estiver presente (nulla ou em branco) sera
     * criado um usuario; mas se a propriedade estiver presente, sera atualizado
     * o usuario com esse ID.
     *
     * Nao necessita ter todas as propriedades na requisicao.
     *
     * @param user - Objeto Usuario a ser persistido ou atualizado no banco
     * @return Objeto User, com todos as propriedades (em branco ou preenchidas)
     */
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario user) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.atualizar(user);
    }

    /**
     * Listagem de TODOS os usuarios cadastrados no Banco de dados.
     *
     * Caso desejar ordenar por nome em ordem alfabetica, passar o valor TRUE
     * senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem
     * alfabetica
     * @return ResponseEntity com um Array em JSON com varios objetos USUARIO
     */
    @GetMapping("/listar/{ordenar}")
    public ResponseEntity<?> listagem(@PathVariable boolean ordenar) {
        // retorna todos os usuarios cadastrados.
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.listar(ordenar);
    }

    /**
     * Busca todos os dados de um Usuario a partir do seu e-mail
     *
     * @param email endereco de email do usuario
     * @return ResponseEntity - Contendo uma mensagem de erro ou um ojbeto JSON
     * contendo todos os dados cadastrados
     */
    @GetMapping("/buscar/{email}")
    public ResponseEntity<?> buscarPeloEmail(@PathVariable String email) {
        // retorna UM usuario com o email informado.
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.selecionar(email);
    }

    /**
     * Busca todos os dados de um Usuario a partir do seu ID
     *
     * @param usuarioID ID do usuario
     * @return ResponseEntity - Contendo uma mensagem de erro ou um ojbeto JSON
     * contendo todos os dados cadastrados
     */
    @GetMapping("/buscarID/{usuarioID}")
    public ResponseEntity<?> buscarPeloEmail(@PathVariable UUID usuarioID) {
        // retorna UM usuario com o ID informado.
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.selecionar(usuarioID);
    }

    /**
     * Valida a senha do usuario para login no sistema
     *
     * @param user - Objeto usuario com o login e senha
     * @return ResponseEntity - Contendo uma mensagem de erro (Login Invalido)
     * ou um objeto Usuario (usuario logado)
     */
    @PostMapping("/senha")
    public ResponseEntity<?> validarSenhas(@RequestBody Usuario user) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.validarSenha(user.getPassword(), user.getEmail());
    }

    /**
     * ARQUIVAR (Desabilitar) um usuário para nao acessar o sistema
     *
     * @param usuarioID - ID do usuario a ser processado
     * @return ResponseEntity - Contendo uma mensagem de erro ou sucesso
     */
    @DeleteMapping("/arquivar/{email}")
    public ResponseEntity<?> arquivarUsuario(@PathVariable UUID usuarioID) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.arquivar(usuarioID, 0);
    }

    /**
     * ATIVAR um usuário para acessar o sistema
     *
     * @param usuarioID - ID do usuario a ser processado
     * @return ResponseEntity - Contendo uma mensagem de erro ou sucesso
     */
    @DeleteMapping("/ativar/{email}")
    public ResponseEntity<?> ativarUsuario(@PathVariable UUID usuarioID) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.arquivar(usuarioID, 1);
    }

    /**
     * DELETAR fisicamente um usuário da base de dados do sistema
     *
     * @param usuarioID - ID do usuario a ser processado
     * @return ResponseEntity - Contendo uma mensagem de erro ou sucessoo
     */
    @DeleteMapping("/bloquear/{email}")
    public ResponseEntity<?> deleteUsuario(@PathVariable UUID usuarioID) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return userService.arquivar(usuarioID, 2);
    }

}
