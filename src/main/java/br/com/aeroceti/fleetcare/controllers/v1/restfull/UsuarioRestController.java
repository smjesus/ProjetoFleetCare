/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.restfull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.aeroceti.fleetcare.services.UsuarioService;
import br.com.aeroceti.fleetcare.model.dto.UsuarioDTO;
import br.com.aeroceti.fleetcare.model.dto.MensagemDTO;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/v1/usuario/")
@Tag(name = "02 - Usuários: ", description = "🔹Entry Point para manipular o objeto Usuario.")
public class UsuarioRestController {

    @Autowired
    private UsuarioService userService;

    private final Logger logger = LoggerFactory.getLogger(UsuarioRestController.class);

    /**
     * Retorna o Token CSRF para uso na API.
     * 
     * @param request Objeto da sessao HTTP
     * @return Token CSRF
     */
    @GetMapping("/autorizacao")
    @Operation(summary = "Obtem o Token de Acesso CSRF.", description = "Este Entry Point obtem um token CSRF para uso nas operacoes POST/PUT/DELETE da API.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com um Token CSRF", content = @Content(schema = @Schema(implementation = CsrfToken.class)))
    })
    public CsrfToken permissaoToken(HttpServletRequest request) {
        logger.info("Enviando Token CSRF para aceeso a API do FleetCare...");
        return (CsrfToken) request.getAttribute("_csrf");
    }
    
    /**
     * Listagem de TODOS os usuarios cadastrados no Banco de dados.
     *
     * Caso desejar ordenar por nome em ordem alfabetica, passar o valor TRUE
     * senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return ResponseEntity com um Array em JSON com varios objetos USUARIO
     */
    @GetMapping("/listar/{ordenar}")
    @Operation(summary = "Listar todos os usuários.", description = "Este Entry Point obtem uma listagem (ordenada ou não) de todos os usuários cadastrados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista dos Usuários ou uma lista vazia (caso nao tenha dados cadastrados).", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class))))
    })
    public ResponseEntity<?> listagem(@PathVariable boolean ordenar) {
        logger.info("Recebida requisicao na API para listar todos os Usuarios ...");
        return new ResponseEntity<>(userService.listar(ordenar), HttpStatus.OK);
    }

    /**
     * Busca um Usuario a partir do seu ID
     *
     * @param usuarioID ID do usuario
     * @return ResponseEntity - Contendo uma mensagem de erro ou um ojbeto JSON
     * contendo todos os dados cadastrados
     */
    @GetMapping("/{usuarioID}")
    @Operation(summary = "Busacar um usuario pelo ID.", description = "Este Entry Point obtem um usuário cadastrado através do ID fornecido.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com o Usuário cadastrados.", content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> buscarPeloID(@PathVariable Long usuarioID) {
        logger.info("Requisicao no UserController para ativar servico: Busca por ID ...");
        Optional<Usuario> usuarioSolicitado = userService.buscar(usuarioID);
        if( usuarioSolicitado.isPresent() ) {
            logger.info("Retornando com o usuario: {}", usuarioSolicitado.get().toString());
        } else {
            logger.info("Nenhum registro encontrado com o ID informado!");
            return new ResponseEntity<>(new MensagemDTO("Nenhum registro encontrado com o ID informado!"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioSolicitado.get(), HttpStatus.OK);
    }
    
    /**
     * Busca um Usuario a partir do seu e-mail
     *
     * @param email endereco de email do usuario
     * @return ResponseEntity - Contendo uma mensagem de erro ou um ojbeto JSON
     * contendo todos os dados cadastrados
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Busacar um usuario pelo EMAIL.", description = "Este Entry Point obtem um usuário cadastrado através do EMAIL fornecido.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com o Usuário cadastrados.", content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> buscarPeloEmail(@PathVariable String email) {
        logger.info("Requisicao no UserController para ativar servico: Busca por email ...");
        Optional<Usuario> usuarioSolicitado = userService.selecionarEmail(email);
        if( usuarioSolicitado.isPresent() ) {
            logger.info("Retornando com o usuario: {}", usuarioSolicitado.get().toString());
        } else {
            logger.info("Nenhum registro encontrado com o email informado!");
            return new ResponseEntity<>(new MensagemDTO("Nenhum registro encontrado com o EMAIL informado!"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioSolicitado.get(), HttpStatus.OK);
    }

    /**
     * Busca um Usuario a partir do seu CPF
     *
     * @param cpf - CPF do usuario
     * @return ResponseEntity - Contendo uma mensagem de erro ou um ojbeto JSON
     * contendo todos os dados cadastrados
     */
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Busacar um usuario pelo CPF.", description = "Este Entry Point obtem um usuário cadastrado através do CPF fornecido.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com o Usuário cadastrados.", content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> buscarPeloCPF(@PathVariable String cpf) {
        logger.info("Requisicao no UserController para ativar servico: Busca por CPF ...");
        Optional<Usuario> usuarioSolicitado = userService.selecionarCPF(cpf.replaceAll("\\D", "").trim());
        if( usuarioSolicitado.isPresent() ) {
            logger.info("Retornando com o usuario: {}", usuarioSolicitado.get().toString());
        } else {
            logger.info("Nenhum registro encontrado com o CPF informado!");
            return new ResponseEntity<>(new MensagemDTO("Nenhum registro encontrado com o CPF informado!"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioSolicitado.get(), HttpStatus.OK);
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Cria um Usuario na base de dados.
     *
     * Esta funcao cria um usuario na base de dados.
     *
     * Nao necessita ter todas as propriedades na requisicao.
     * Propriedades obrigatorias: Nome, E-mail e CPF (valores validos).
     *
     * @param user - Objeto Usuario a ser persistido ou atualizado no banco
     * @return Objeto User (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/cadastrar")
//    @Operation(summary = "Cadastrar usuário", description = "Este Entry Point cadastra um usuário no sistema", tags = { "Usuários" })
//    @ApiResponses(value = { 
//        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))),
//        @ApiResponse(responseCode = "400", description = "Solicitaçao não realizada.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MensagemDTO.class))))
//    })
    public ResponseEntity<?> cadastrarUsuario(/* @Parameter(description = "Dados principais a serem cadastrados para um usuário.", required = true) */ @RequestBody UsuarioDTO user) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return null; // userService.cadastrar(user);
    }

    /**
     * Atualiza um Usuario na base de dados.
     *
     * Atualiza um usuario (com ID valido) que esteja na base de dados.
     *
     * Nao necessita ter todas as propriedades na requisicao.
     * Propriedades obrigatorias: Nome, E-mail e CPF (valores validos).
     *
     * @param user - Objeto Usuario a ser persistido ou atualizado no banco
     * @return Objeto User (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/atualizar")
//    @Operation(summary = "Atualizar usuário", description = "Este Entry Point atualiza os dados de um usuário no sistema", tags = { "Usuários" })
//    @ApiResponses(value = { 
//        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))),
//        @ApiResponse(responseCode = "400", description = "Solicitaçao não realizada.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MensagemDTO.class))))
//    })
    public ResponseEntity<?> atualizarUsuario(@RequestBody UsuarioDTO user) {
        logger.info("Requisicao no UserController para ativar servico ...");
        return null; // userService.atualizar(user);
    }


    
    
    
    
    

    /**
     * DESATIVAR um usuário para nao acessar o sistema
     *
     * @param usuarioID - ID do usuario a ser processado
     * @return ResponseEntity - Contendo uma mensagem de erro ou sucesso
     */
    @PutMapping("/desativar/{usuarioID}")
    @Operation(summary = "Desativar usuário", description = "Este Entry Point desabilita um usuário no sistema, impossibilitando o Login do mesmo.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma mensagem de SUCESSO do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de ERRO no processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> arquivarUsuario(@PathVariable Long usuarioID) {
        logger.info("Requisicao no UserController para ativar servico: DESATIVAR usuário ...");
        return userService.arquivar(usuarioID, 0);
    }

    /**
     * ATIVAR um usuário para acessar o sistema
     *
     * @param usuarioID - ID do usuario a ser processado
     * @return ResponseEntity - Contendo uma mensagem de erro ou sucesso
     */
    @Operation(summary = "Ativar usuário", description = "Este Entry Point ativa a conta de um usuário no sistema, possibilitando o Login do mesmo.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma mensagem de SUCESSO do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de ERRO no processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    @PutMapping("/ativar/{usuarioID}")
    public ResponseEntity<?> ativarUsuario(@PathVariable Long usuarioID) {
        logger.info("Requisicao no UserController para ativar servico: ATIVAR usuário ...");
        return userService.arquivar(usuarioID, 1);
    }

    /**
     * DELETAR fisicamente um usuário da base de dados do sistema
     *
     * @param usuarioID - ID do usuario a ser processado
     * @return ResponseEntity - Contendo uma mensagem de erro ou sucessoo
     */
    @Operation(summary = "DELETAR usuário", description = "Este Entry Point DELETA um usuário no sistema.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma mensagem de SUCESSO do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de ERRO no processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    @DeleteMapping("/bloquear/{usuarioID}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long usuarioID) {
        logger.info("Requisicao no UserController para ativar servico: DELETAR usuário ...");
        return userService.arquivar(usuarioID, 2);
    }

}
/*                    End of Class                                            */