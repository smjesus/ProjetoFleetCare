/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.web;

import br.com.aeroceti.fleetcare.model.carro.Fabricante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import br.com.aeroceti.fleetcare.services.FabricantesService;
import br.com.aeroceti.fleetcare.services.I18nService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Classe Controller para o objeto Fabricantes.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/fabricante/")
public class FabricanteController {

    @Autowired
    private I18nService i18svc;
    @Autowired
    private FabricantesService fabricaSVC;

    private final Logger logger = LoggerFactory.getLogger(FabricanteController.class);

    /**
     * Listagem de TODAS as Fabricas cadastradas no Banco de dados.
     * 
     * Caso desejar ordenar por nome em ordem alfabetica, 
     * passar o valor TRUE senao FALSE
     *
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @RequestMapping("/listar/{ordenar}")
    public String listagem(Model modelo, @PathVariable boolean ordenar) {
        logger.info("Recebida requisicao para listar os Fabricantes de Carros cadastrados...");
        modelo.addAttribute("colecao",fabricaSVC.listar(ordenar));
        return"/model/carro/fabricantesListagem";
    }
    
    /**
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados (PAGINADA).
     * 
     * @param modelo - Objeto Model para injetar dados na View
     * @param page     numero da pagina em exibicao
     * @param pageSize total de itens na pagina
     * @return ModelAndView preparada pelo Thymeleaf com o objeto para listagem.
     */
    @RequestMapping("/paginar/{page}/{pageSize}")
    public String listar( Model modelo, @PathVariable int page, @PathVariable int pageSize ) {
        logger.info("Recebida requisicao para listar as Permissoes PAGINADAS ...");
        modelo.addAttribute("colecao",fabricaSVC.paginar(page, pageSize));
        return"/model/carro/fabricantes";
    }
    
    /**
     * FORMULARIO de cadastro de Fabricantes na base de dados.
     *
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um fabricante.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrarFabricantes(Model modelo) {
        modelo.addAttribute("fabricante", new Fabricante());
        logger.info("Recebida requisicao para cadastro:  encaminhando ao formulario ...");
        return "/model/carro/fabricantesCadastrar";
    }

    /**
     * Metodo para gravar um Fabricante na base de dados.
     * 
     * Esta funcao envia os dados do Cadastro (Formulario) de um fabricante para o banco de dados.
     *
     * @param fabricante - Objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * @param modelo
     * @param locale
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/gravar")
    public String salvarFabricante(@Valid Fabricante fabricante, BindingResult result, RedirectAttributes atributes, Model modelo, Locale locale) {
        logger.info("Recebida requisicao para gravar um cadastro.");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("GRAVAÇÃO NÃO REALIZADA - Erro na validação! ");
            return "/model/carro/fabricantesCadastrar" ;
        }
        // Se nao houver erros, salva os dados:
        if ( fabricaSVC.isFabricanteCadastrado(fabricante.getFabricanteName()) ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Fabricante já Existe! ");
            modelo.addAttribute("mensagem", i18svc.buscarMensagem("roles.erro.exists", locale) );
            return "/model/carro/fabricantesCadastrar" ;
        }
        fabricante.setEntidadeID(null);
        fabricaSVC.salvar(fabricante);
        return "redirect:/fabricante/paginar/1/15";
    }
    
    /**
     * FORMULARIO para atualizar um Fabricante na base de dados.
     * 
     * @param id - ID do objeto a ser persistido
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/atualizar/{id}")
    public String atualizarFabricantes( @PathVariable("id") long id, Model modelo ) {
        Optional<Fabricante> fabricanteSolicitada = fabricaSVC.buscarFabricante(id);
        if( fabricanteSolicitada.isPresent() ) {
            Fabricante fabricante = fabricanteSolicitada.get();
            logger.info("Requisicao para ALTERARAR os dados de: {}.", fabricante.getFabricanteName());
            modelo.addAttribute("fabricante", fabricante);
        } else {
            logger.info("Requisicao de ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return "redirect:/fabricante/paginar/1/15";
        }
        return "/model/carro/fabricantesEditar"; 
    }

    /**
     * Atualiza um Fabricante da base de dados.
     * 
     * Atualiza um Fabricante (com ID valido) que esteja na base de dados.
     * 
     * @param id - ID do fabricante
     * @param fabricante - objeto com os dados para atualizar 
     * @param result  - objeto do contexto HTTP
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar/{id}")
    public String atualizarFabricantes(@PathVariable("id") long id, @Valid Fabricante fabricante, BindingResult result) {
        fabricante.setEntidadeID(id);
        if (result.hasErrors()) {
            logger.info("Requisicao de ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "/model/carro/fabricantesEditar";
        }
        //fabricaRepository.save(fabricante);
        logger.info("Requisicao de ALTERÇÃO REALIZADA com sucesso! ");
        return "redirect:/fabricante/paginar/1/15";
    }

    /**
     * DELETA um Fabricante da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletarFabricantes( @PathVariable("id") long id, Model modelo ) {
        Optional<Fabricante> fabricanteSolicitada = null; //fabricaRepository.findById(id);
        if( fabricanteSolicitada.isPresent() ) {
            Fabricante fabricante = fabricanteSolicitada.get();
            logger.info("Requisicao no FabricanteController: DELETANDO fabricante: " + fabricante.getFabricanteName());
//        // remove a fabricante dos usuarios:
//        for ( Usuario book : fabricante.getUsuarios() ) {
//            fabricante.removeUsuario(book);
//        }
          //  fabricaRepository.delete(fabricante);
        }
        return "redirect:/fabricante/listar/0";
    }

}
/*                    End of Class                                            */