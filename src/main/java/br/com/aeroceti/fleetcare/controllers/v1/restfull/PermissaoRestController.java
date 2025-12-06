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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import br.com.aeroceti.fleetcare.model.dto.MensagemDTO;
import br.com.aeroceti.fleetcare.model.user.NivelAcesso;
import br.com.aeroceti.fleetcare.model.dto.NivelAcessoDTO;
import br.com.aeroceti.fleetcare.services.PermissoesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Classe REST Controller para o objeto NivelAcesso (Permissoes).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/permissao/")
@Tag(name = "01 - Nivel de Acesso: ", description = "🔹Entry Point para manipular o objeto NivelAcesso (Permissao).")
public class PermissaoRestController {
    
    @Autowired
    private PermissoesService permissaoService;

    private final Logger logger = LoggerFactory.getLogger(PermissaoRestController.class);

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
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados.
     *
     * Caso desejar ordenar o nome em ordem alfabetica, passar o valor TRUE
     * senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return ArrayList em JSON com os objetos cadastrados
     */
    @GetMapping("/listar/{ordenar}")
    @Operation(summary = "Listar todos os Niveis de Acesso.", description = "Este Entry Point obtem uma listagem (ordenada ou não) de todos as permissões (Niveis de Acesso) cadastradas.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista das Permissões ou uma lista vazia (caso nao tenha dados cadastrados)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NivelAcesso.class))))
    })
    public ResponseEntity<?> listagem(@PathVariable boolean ordenar) {
        logger.info("Recebida requisicao na API para listar todas as Permissoes ...");
        return new ResponseEntity<>( permissaoService.listar(ordenar), HttpStatus.OK);
    }
    
    /**
     * Obtem um Nivel de Acesso da base de dados pelo NOME informado.
     *
     * @param nome - nome do nivel de acesso que se procura
     * @return Objeto Nivel de Acesso (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @GetMapping("/nome/{nome}")
    @Operation(summary = "APRESENTA um Nivel de Acesso do Banco de Dados.", description = "Este Entry Point busca as informações no banco de dados de um Nivel de Acesso conforme o NOME informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados do Nivel de Acesso encontrado", content = @Content(schema = @Schema(implementation = NivelAcesso.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> obterPermissao(@PathVariable String nome) {
        NivelAcesso permissaoInformada;
        logger.info("Obtendo uma permissao com o nome de " + nome);
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissaoPorNome(nome);
        if( permissaoSolicitada.isPresent() ) {
            permissaoInformada = permissaoSolicitada.get();
        } else {
            logger.info("Requisicao no PermissaoController: Nivel de Acesso NAO ENCONTRADO! ");
            return new ResponseEntity<>(new MensagemDTO("Nivel de Acesso NÃO Encontrado!"), HttpStatus.BAD_REQUEST);
        }
        logger.info("Enviando a resposta ao usuario... ");
        return new ResponseEntity<>(permissaoInformada, HttpStatus.OK);
    }
    
    /**
     * Obtem um Nivel de Acesso da base de dados pelo ID informado.
     *
     * @param id - entidadeID do nivel de acesso que se procura
     * @return Objeto Nivel de Acesso (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @GetMapping("/{id}")
    @Operation(summary = "APRESENTA um Nivel de Acesso do Banco de Dados.", description = "Este Entry Point busca as informações no banco de dados de um Nivel de Acesso conforme o ID informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados do Nivel de Acesso encontrado", content = @Content(schema = @Schema(implementation = NivelAcesso.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> obterPermissao(@PathVariable Long id) {
        NivelAcesso permissaoInformada;
        logger.info("Obtendo uma permissao com o ID = " + id);
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissao(id);
        if( permissaoSolicitada.isPresent() ) {
            permissaoInformada = permissaoSolicitada.get();
        } else {
            logger.info("Requisicao no PermissaoController: Nivel de Acesso NAO ENCONTRADO! ");
            return new ResponseEntity<>(new MensagemDTO("Nivel de Acesso NÃO Encontrado!"), HttpStatus.BAD_REQUEST);
        }
        logger.info("Enviando a resposta ao usuario... ");
        return new ResponseEntity<>(permissaoInformada, HttpStatus.OK);
    }

    /**
     * Cria um Nivel de Acesso na base de dados.
     *
     * Esta funcao cria um Nivel de Acesso na base de dados.
     *
     * @param permissao - Objeto Nivel de Acesso a ser persistido ou atualizado no banco
     * @return Objeto Nivel de Acesso (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PostMapping("/cadastrar")
    @Operation(summary = "Grava um Nivel de Acesso no Banco de Dados.", description = "Este Entry Point valida os dados recebidos de um Nivel de Acesso e (se corretos) grava no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados do Nivel de Acesso salvo",     content = @Content(schema = @Schema(implementation = NivelAcesso.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento", content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> cadastrarPermissao(@RequestBody NivelAcessoDTO permissao) {
        logger.info("Requisicao para GRAVAR um novo objeto NivelAcesso: {} ", permissao.toString());
        if (permissao.nome().equals("")) {
            logger.info("Dados nao cadastrados: NOME da Permissao precisa ser preenchido!");
            return new ResponseEntity<>(new MensagemDTO("NOME da Permissao precisa ser preenchido!"), HttpStatus.BAD_REQUEST);
        }        
        if ( permissaoService.isPermissaoCadastrada(permissao.nome()) ) {
            logger.info("Dados nao cadastrados: Permissao já Existe! ");
            return new ResponseEntity<>(new MensagemDTO("Dados nao cadastrados: Permissao já Existe!"), HttpStatus.BAD_REQUEST);
        }
        // Salva no Banco de dados atraves da Classe de Servico
        NivelAcesso perfil = new NivelAcesso(null);
        perfil.setNome(permissao.nome());
        return permissaoService.salvar(perfil);
    }

    /**
     * Atualiza um Nivel de Acesso na base de dados.
     *
     * @param permissao - Objeto Nivel de Acesso a ser atualizado no banco
     * @return Objeto Nivel de Acesso (atualizado) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/atualizar")
    @Operation(summary = "Atualiza um Nivel de Acesso do Banco de Dados.", description = "Este Entry Point valida os dados recebidos de um Nivel de Acesso e (se corretos) atualiza os dados no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados do Nivel de Acesso atualizado", content = @Content(schema = @Schema(implementation = NivelAcesso.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de erro do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> salvarPermissao(@RequestBody NivelAcessoDTO permissao) {
        logger.info("Requisicao para ATUALIZAR um objeto NivelAcesso: {} ", permissao.toString());
        
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissao(permissao.entidadeID());
        if( permissaoSolicitada.isPresent() ) {
            NivelAcesso permissaoAntiga = permissaoSolicitada.get();
            logger.info("ALTERANDO  os dados de " + permissaoAntiga.getNome());
            permissaoAntiga.setNome(permissao.nome());
            permissaoService.atualizar(permissaoAntiga);
        } else {
            logger.info("ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return new ResponseEntity<>(new MensagemDTO("Dados nao Atualuzados: Referencia Invalida!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(permissaoSolicitada, HttpStatus.OK);
    }

    /**
     * DELETA um Nivel de Acesso da base de dados pelo ID informado.
     *
     * @param id - entidadeID do Nivel de Acesso que se procura
     * @return Objeto Nivel de Acesso (REMOVIDO) ou MensagemDTO (com o erro ocorrido)
     */
    @DeleteMapping("/remover/{id}")
    @Operation(summary = "DELETA um Nivel de Acesso do Banco de Dados.", description = "Este Entry Point valida os dados recebidos de um Nivel de Acesso e (se corretos) EXCLUI a permissao informada no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma mensagem de Sucesso do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class))),
        @ApiResponse(responseCode = "400", description = "ResponseEntity com uma mensagem de ERRO do processamento",  content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    })
    public ResponseEntity<?> deletarPermissao(@PathVariable Long id) {
        NivelAcesso permissaoInformada;
        logger.info("Requisicao para DELETAR um objeto NivelAcesso: ID={} ", id);
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissao(id);
        if( permissaoSolicitada.isPresent() ) {
            permissaoInformada = permissaoSolicitada.get();
            // remove a permissao dos usuarios:
            if( !permissaoInformada.getUsuarios().isEmpty() ) {
                for ( Usuario user : permissaoInformada.getUsuarios() ) {
                    user.setNivelAcesso(null);
                }
            }
        } else {
            logger.info("ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return new ResponseEntity<>(new MensagemDTO("Dados NAO DELETADOS: Referencia Invalida!"), HttpStatus.BAD_REQUEST);
        }
        return permissaoService.removerPermissao(permissaoInformada);
    }

}
/*                    End of Class                                            */