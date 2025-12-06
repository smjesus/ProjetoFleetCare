/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.web;

import br.com.aeroceti.fleetcare.model.carro.Carro;
import br.com.aeroceti.fleetcare.model.carro.Modelo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import br.com.aeroceti.fleetcare.repositories.CarroRepository;
import br.com.aeroceti.fleetcare.repositories.ModeloRepository;
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
 * Classe Controller para o objeto Carro.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/carro/")
public class CarroController {

    @Autowired
    private final CarroRepository carroRepository;
    @Autowired
    private final ModeloRepository modeloRepository;
    
    private final Logger logger = LoggerFactory.getLogger(CarroController.class);

    public CarroController(CarroRepository fabricaRep, ModeloRepository modRep) {
        this.carroRepository = fabricaRep;
        this.modeloRepository= modRep ;
    }

    /**
     * Listagem de TODOS os Carross cadastrados no Banco de dados.
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
        logger.info("Requisicao no CarroController: listando todas os carros ...");
        List<Carro> listagem;
        if( ordenar ) {
            logger.info("Requisicao no CarroController: listagem ordenada dos carros ...");
            listagem = carroRepository.findByOrderByNumeroDaPlacaAsc();
        } else {
            logger.info("Requisicao no CarroController: listagem sem ordenacao requerida ...");
            listagem = carroRepository.findAll();
        } 
        modelo.addAttribute("carros",listagem);
        return"/model/carro/veiculos";
    }
    
    /**
     * Formulario de cadastro de Carros na base de dados.
     *
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um Carro.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrarCarros(Model modelo) {
        List<Modelo> modelosCadastradas = modeloRepository.findAll();
        modelo.addAttribute("carro", new Carro());
        modelo.addAttribute("modelos", modelosCadastradas);
        logger.info("Requisicao no CarroController para cadastro:  encaminhando ao formulario ...");
        return "/model/carro/veiculosCadastrar";
    }

    /**
     * Metodo para gravar um Carro na base de dados.
     * 
     * Esta funcao envia os dados do Cadastro (Formulario) de um Carro para o banco de dados.
     *
     * @param carro  - Objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/gravar")
    public String salvarCarros(@Valid Carro carro, BindingResult result, RedirectAttributes atributes) {
        logger.info("Requisicao no CarroController para gravar um cadastro.");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("Requisicao no CarroController: GRAVAÇÃO NÃO REALIZADA - Erro na validação! ");
            return "/model/carro/veiculosCadastrar" ;
        }
        // Se nao houver erros, salva os dados:
        carroRepository.save(carro);
        atributes.addFlashAttribute("mensagem", "Carro cadastrado com sucesso!");
        return "redirect:/carro/cadastrar";
    }
    
    /**
     * Apresenta o formulario para atualizar um Carro na base de dados.
     * 
     * @param id - ID do objeto a ser persistido
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/atualizar/{id}")
    public String atualizarCarros( @PathVariable("id") long id, Model modelo ) {
        Optional<Carro> veiculoSolicitado = carroRepository.findById(id);
        if( veiculoSolicitado.isPresent() ) {
            Carro carro = veiculoSolicitado.get();
            logger.info("Requisicao no CarroController: ALTERANDO  os dados do Veiculo com placa: " + carro.getNumeroDaPlaca());
            modelo.addAttribute("carro", carro);
        } else {
            logger.info("Requisicao no CarroController: ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return "redirect:/carro/listar/0";
        }
        return "/model/carro/veiculosEditar"; 
    }

    /**
     * Atualiza um Carro da base de dados.
     * 
     * Atualiza um carro (com ID valido) que esteja na base de dados.
     * 
     * @param id - ID do 
     * @param carro - objeto a ser persistido 
     * @param result  - objeto do contexto HTTP
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar/{id}")
    public String atualizarCarros(@PathVariable("id") long id, @Valid Carro carro, BindingResult result) {
        carro.setCarroID(id);
        if (result.hasErrors()) {
            logger.info("Requisicao no CarroController: ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "/model/carro/veiculosEditar";
        }
        carroRepository.save(carro);
        logger.info("Requisicao no CarroController: ALTERÇÃO REALIZADA com sucesso! ");
        return "redirect:/carro/listar/0";
    }

    /**
     * DELETA um Carro da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletarCarros( @PathVariable("id") long id, Model modelo ) {
        Optional<Carro> veiculoSolicitado = carroRepository.findById(id);
        if( veiculoSolicitado.isPresent() ) {
            Carro veiculoParaDeletar = veiculoSolicitado.get();
            logger.info("Requisicao no CarroController: DELETANDO carro com placa: " + veiculoParaDeletar.getNumeroDaPlaca());
//        // remove a fabricante dos usuarios:
//        for ( Usuario book : fabricante.getUsuarios() ) {
//            fabricante.removeUsuario(book);
//        }
            carroRepository.delete(veiculoParaDeletar);
        }
        return "redirect:/carro/listar/0";
    }

}
/*                    End of Class                                            */