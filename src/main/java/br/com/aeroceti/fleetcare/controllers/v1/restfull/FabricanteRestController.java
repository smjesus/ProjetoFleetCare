/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.restfull;

import br.com.aeroceti.fleetcare.model.dto.FabricaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.aeroceti.fleetcare.services.FabricantesService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe REST Controller para o objeto Rules (Fabricas).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
//@RestController
@RequestMapping("/api/v1/fabricante/")
//@Tag(name = "Fabricantes", description = "Entry Point para manipular o objeto Fabricante")
@CrossOrigin(origins = {"http://127.0.0.1:5173/", "https://victornunes-me.github.io/" })
public class FabricanteRestController {
    
    @Autowired
    private final FabricantesService fabricanService;

    private final Logger logger = LoggerFactory.getLogger(FabricanteRestController.class);

    public FabricanteRestController(FabricantesService rulesServices) {
        this.fabricanService = rulesServices;
    }

    /**
     * Listagem de TODAS as Fabricas cadastradas no Banco de dados.
     *
     * Caso desejar ordenar o nome em ordem alfabetica, passar o valor TRUE
     * senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return ResponseEntity com um Array em JSON com varios objetos USUARIO
     */
    @GetMapping("/listar/{ordenar}")
//    @Operation(summary = "Apresenta uma listagem de todas as fabricas")
//    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista de Fabricas"))
    public ResponseEntity<?> listagem(@PathVariable boolean ordenar) {
        // retorna todos os usuarios cadastrados.
        logger.info("Requisicao no FabricanteRestController para ativar servico ...");
        return null; //fabricanService.listar(ordenar);
    }

    /**
     * Cria um Fabricante na base de dados.
     *
     * Esta funcao cria um Fabricante na base de dados.
     *
     * @param fabrica - Objeto Fabricante a ser persistido ou atualizado no banco
     * @return Objeto Fabricante (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/cadastrar")
    public ResponseEntity<?> cadastrarFabricante(@RequestBody FabricaDTO fabrica) {
        logger.info("Requisicao no FabricanteController para ativar servico ...");
        return null; // fabricanService.cadastrar(fabrica);
    }

    /**
     * Atualiza um Fabricante na base de dados.
     *
     * @param fabrica - Objeto Fabricante a ser persistido ou atualizado no banco
     * @return Objeto Fabricante (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/atualizar")
    public ResponseEntity<?> salvarFabricante(@RequestBody FabricaDTO fabrica) {
        logger.info("Requisicao no FabricanteController para ativar servico ...");
        return null; //  fabricanService.atualizar(fabrica);
    }

    /**
     * Obtem um Fabricante da base de dados pelo NOME informado.
     *
     * @param nome - nome da fabrica que se procura
     * @return Objeto Fabricante (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> obterFabricante(@PathVariable String nome) {
        logger.info("Requisicao no FabricanteController para ativar servico ...");
        return  null; // fabricanService.selecionar(nome);
    }
    
    /**
     * Obtem um Fabricante da base de dados pelo ID informado.
     *
     * @param id - ID da fabrica que se procura
     * @return Objeto Fabricante (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obterFabricante(@PathVariable Long id) {
        logger.info("Requisicao no FabricanteController para ativar servico ...");
        return null; //  fabricanService.selecionar(id);
    }

    /**
     * DELETA um Fabricante da base de dados pelo ID informado.
     *
     * @param id - ID da fabrica que se procura
     * @return Mensagem, HTTP.Status
     */
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletarFabricante(@PathVariable Long id) {
        logger.info("Requisicao no FabricanteController para ativar servico ...");
        return null; //  fabricanService.removerFabricante(id);
    }    
    
}
/*                    End of Class                                            */