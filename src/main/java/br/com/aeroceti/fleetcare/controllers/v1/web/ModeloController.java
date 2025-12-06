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
import br.com.aeroceti.fleetcare.model.carro.Modelo;
import br.com.aeroceti.fleetcare.repositories.FabricanteRepository;
import br.com.aeroceti.fleetcare.repositories.ModeloRepository;
import java.util.Objects;
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
 * Classe Controller para o objeto Modelos.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/modelo/")
public class ModeloController {

    @Autowired
    private final ModeloRepository modeloRepository;
    @Autowired
    private final FabricanteRepository fabricaRepository;
    
    private final Logger logger = LoggerFactory.getLogger(ModeloController.class);

    public ModeloController(ModeloRepository modeloRep, FabricanteRepository factoryRep) {
        this.modeloRepository  = modeloRep;
        this.fabricaRepository = factoryRep;
    }

    /**
     * Listagem de TODAS os Modelos cadastradas no Banco de dados.
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
        logger.info("Requisicao no ModeloController: listando todas os modelos ...");
        List<Modelo> listagem;
        if( ordenar ) {
            logger.info("Requisicao no ModeloController: listagem ordenada das modelos ...");
            listagem = modeloRepository.findByOrderByModeloNameAsc();
        } else {
            logger.info("Requisicao no ModeloController: listagem sem ordenacao requerida ...");
            listagem = modeloRepository.findAll();
        } 
        modelo.addAttribute("modelos",listagem);
        return"/model/carro/modelos";
    }
    
    /**
     * Formulario de cadastro de Modelos na base de dados.
     *
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um modelo.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrarModelos(Model modelo) {
        List<Fabricante> fabricasCadastradas = fabricaRepository.findAll();
        modelo.addAttribute("modelo", new Modelo());
        modelo.addAttribute("fabricantes", fabricasCadastradas);
        
        logger.info("Requisicao no ModeloController para cadastro:  encaminhando ao formulario ...");
        return "/model/carro/modelosCadastrar";
    }

    /**
     * Metodo para gravar um Modelo na base de dados.Esta funcao envia os dados do Cadastro (Formulario) de um modelo para o banco de dados.
     * 
     *
     * @param modelo - Objeto a ser persistido no banco
     * @param fabricante - Objeto de relacionamento
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/gravar")
    public String salvarModelos(Modelo modelo, Fabricante fabricante, BindingResult result, RedirectAttributes atributes) {
        logger.info("Requisicao no ModeloController para gravar um cadastro.");
        // Se houver erros retorna ao formulario:
        if ( Objects.isNull(modelo.getModeloName()) ) {
            atributes.addAttribute("errorMessage", "Modelo tem que ter NOME!");
            List<Fabricante> fabricasCadastradas = fabricaRepository.findAll();
            atributes.addAttribute("fabricantes", fabricasCadastradas);
            logger.info("Requisicao no ModeloController: GRAVAÇÃO NÃO REALIZADA - Nome em branco! ");
            return "/model/carro/modelosCadastrar" ;
        }
        if ( Objects.isNull(fabricante.getEntidadeID()) ) {
            List<Fabricante> fabricasCadastradas = fabricaRepository.findAll();
            atributes.addAttribute("fabricantes", fabricasCadastradas);
            atributes.addAttribute("errorMessage", "Fabricante não foi selecionado!");
            logger.info("Requisicao no ModeloController: GRAVAÇÃO NÃO REALIZADA - Fabricante não foi selecionado! ");
            return "/model/carro/modelosCadastrar" ;
        }
        // Obtem o fabricante no banco de dados:
        Optional<Fabricante> fabricaSolicitada = fabricaRepository.findById(fabricante.getEntidadeID());
        if( fabricaSolicitada.isPresent() ) {
            fabricante = fabricaSolicitada.get();
            modelo.setFabricante(fabricante);
            //fabricante.addModelo(modelo);
            // Se nao houver erros, salva os dados:
            modeloRepository.save(modelo);
            fabricaRepository.save(fabricante);
            atributes.addFlashAttribute("mensagem", "Modelo cadastrado com sucesso!");
        } else {
            atributes.addFlashAttribute("mensagem", "FALHA: Nao obteve o fabricante no Banco de Dados!");
        }
        return "redirect:/modelo/cadastrar";
    }
    
    /**
     * Apresenta o formulario para atualizar um Modelo na base de dados.
     * 
     * @param id - ID do objeto a ser persistido
     * @param model - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/atualizar/{id}")
    public String atualizarModelos( @PathVariable("id") long id, Model model ) {
        Optional<Modelo> modeloSolicitado = modeloRepository.findById(id);
        if( modeloSolicitado.isPresent() ) {
            Modelo modelo = modeloSolicitado.get();
            logger.info("Requisicao no ModeloController: ALTERANDO  os dados do " + modelo.getModeloName());
            model.addAttribute("modelo", modelo);
        } else {
            logger.info("Requisicao no ModeloController: ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return "redirect:/modelo/listar/0";
        }
        return "/model/carro/modelosEditar"; 
    }

    /**
     * Atualiza uma Modelos da base de dados.Atualiza um usuario (com ID valido) que esteja na base de dados.
     * 
     * @param id - ID do 
     * @param modelo - objeto a ser persistido 
     * @param result  - objeto do contexto HTTP
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar/{id}")
    public String atualizarModelos(@PathVariable("id") long id, @Valid Modelo modelo, BindingResult result) {
        modelo.setModeloID(id);
        if (result.hasErrors()) {
            logger.info("Requisicao no ModeloController: ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "/model/carro/modelosEditar";
        }
        modeloRepository.save(modelo);
        logger.info("Requisicao no ModeloController: ALTERÇÃO REALIZADA com sucesso! ");
        return "redirect:/modelo/listar/0";
    }

    /**
     * DELETA uma Modelos da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param model - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletarModelos( @PathVariable("id") long id, Model model ) {
        Optional<Modelo> modeloSolicitada = modeloRepository.findById(id);
        if( modeloSolicitada.isPresent() ) {
            Modelo modelo = modeloSolicitada.get();
            logger.info("Requisicao no ModeloController: DELETANDO modelo: " + modelo.getModeloName());
//        // remove a modelo dos usuarios:
//        for ( Usuario book : modelo.getUsuarios() ) {
//            modelo.removeUsuario(book);
//        }
            modeloRepository.delete(modelo);
        }
        return "redirect:/modelo/listar/0";
    }

}
/*                    End of Class                                            */