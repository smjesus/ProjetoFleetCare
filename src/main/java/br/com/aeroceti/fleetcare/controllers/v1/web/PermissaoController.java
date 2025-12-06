/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.Locale;
import jakarta.validation.Valid;
import br.com.aeroceti.fleetcare.model.user.NivelAcesso;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import br.com.aeroceti.fleetcare.services.I18nService;
import br.com.aeroceti.fleetcare.services.PermissoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Classe Controller para o objeto Permissao.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/permissao/")
public class PermissaoController {

    @Autowired
    private I18nService i18svc;
    
    @Autowired
    private PermissoesService permissaoService;
    
    private final Logger logger = LoggerFactory.getLogger(PermissaoController.class);


    /**
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados.
     * 
     * Caso desejar ordenar por nome em ordem alfabetica, 
     * passar o valor TRUE senao FALSE
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @RequestMapping("/listar/{ordenar}")
    public String listagem(Model modelo, @PathVariable boolean ordenar) {
        logger.info("Recebida requisicao para listar todas as permissoes...");
        modelo.addAttribute("permissoes",permissaoService.listar(ordenar));
        return"/model/usuario/rulesListagem";
    }

    /**
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados (PAGINADA).
     * 
     * @param modelo - Objeto Model para injetar dados na View
     * @param page     numero da pagina em exibicao
     * @param pageSize total de itens na pagina
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @RequestMapping("/paginar/{page}/{pageSize}")
    public String listar( Model modelo, @PathVariable int page, @PathVariable int pageSize ) {
        logger.info("Recebida requisicao para listar as Permissoes PAGINADAS ...");
        modelo.addAttribute("permissoes",permissaoService.paginar(page, pageSize));
        return "/model/usuario/rulesList";
    }
    
    /**
     * FORMULARIO de cadastro de Permissao na base de dados.
     *
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um permissao.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrarPermissao(Model modelo) {
        modelo.addAttribute("nivelAcesso", new NivelAcesso());
        logger.info("Requisicao no PermissaoController para cadastro:  encaminhando ao formulario ...");
        return "/model/usuario/rulesCadastrar";
    }

    /**
     * Metodo para gravar uma Permissao na base de dados.
     * 
     * Esta funcao envia os dados do Cadastro (Formulario) de uma permissao para o banco de dados.
     *
     * @param permissao - Objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param modelo  - Model do Spring WEB
     * @param locale  - Objeto de Inernacionalizacao
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/cadastrar")
    public String salvarPermissao(@Valid NivelAcesso permissao, BindingResult result, Model modelo, Locale locale) {
        logger.info("Recebida requisicao para gravar um cadastro...");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("Requisicao no PermissaoController: GRAVAÇAO NÃO REALIZADA - Erro na Validacao! ");
            return "/model/usuario/rulesCadastrar" ;
        }
        if ( permissaoService.isPermissaoCadastrada(permissao.getNome()) ) {
            logger.info("Requisicao no PermissaoController: GRAVAÇAO NÃO REALIZADA - Permissao já Existe! ");
            modelo.addAttribute("mensagem", i18svc.buscarMensagem("roles.erro.exists", locale) );
            return "/model/usuario/rulesCadastrar" ;
        }
        // Se nao houver erros, salva os dados:
        permissao.setEntidadeID(null);
        permissaoService.salvar(permissao);
        return "redirect:/permissao/paginar/1/15";
    }
    
    /**
     * FORMULARIO para atualizar uma Permissao na base de dados.
     * 
     * @param id - ID do objeto a ser persistido
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/atualizar/{id}")
    public String atualizarPermissao( @PathVariable("id") long id, Model modelo ) {
        logger.info("Recebida requisicao para editar um cadastro...");
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissao(id);
        if( permissaoSolicitada.isPresent() ) {
            NivelAcesso permissao = permissaoSolicitada.get();
            logger.info("Requisicao no PermissaoController: APRESENTANDO os dados para alteracao: " + permissao.getNome());
            modelo.addAttribute("nivelAcesso", permissao);
        } else {
            logger.info("Requisicao no PermissaoController: ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return "redirect:/permissao/paginar/1/15";
        }
        return "/model/usuario/rulesEditar"; 
    }

    /**
     * Atualiza uma Permissao da base de dados.Atualiza um usuario (com ID valido) que esteja na base de dados.
     * 
     * Nao necessita ter todas as propriedades na requisicao.Propriedades obrigatorias: Nome, E-mail e CPF (valores validos).
     * 
     * @param permissao - objeto a ser persistido 
     * @param result  - objeto do contexto HTTP
     * @param modelo  - Model do Spring WEB
     * @param locale  - Objeto de Inernacionalizacao
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar")
    public String atualizarPermissao(@Valid NivelAcesso permissao, BindingResult result, Model modelo, Locale locale ) {
        if (result.hasErrors()) {
            logger.info("Requisicao no PermissaoController: ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            //modelo.addAttribute("permissao", permissao);
            return "/model/usuario/rulesEditar";
        }
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissao(permissao.getEntidadeID());
        if( permissaoSolicitada.isPresent() ) {
            NivelAcesso permissaoAntiga = permissaoSolicitada.get();
            logger.info("Requisicao no PermissaoController: ALTERANDO  os dados do " + permissaoAntiga.getNome());
            permissaoAntiga.setNome(permissao.getNome());
            permissaoService.atualizar(permissaoAntiga);
        } else {
            logger.info("Requisicao no PermissaoController: ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
        }
        return "redirect:/permissao/paginar/1/15";
    }

    /**
     * DELETA uma Permissao da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletarPermissao( @PathVariable("id") long id, Model modelo ) {
        logger.info("Requisicao no PermissaoController: buscando PERMISSAO para DELETAR... " );
        Optional<NivelAcesso> permissaoSolicitada = permissaoService.buscarPermissao(id);
        if( permissaoSolicitada.isPresent() ) {
            NivelAcesso permissao = permissaoSolicitada.get();
            logger.info("Requisicao no PermissaoController: DELETANDO permissao: " + permissao.getNome());
            // remove a permissao dos usuarios:
            for ( Usuario user : permissao.getUsuarios() ) {
                user.setNivelAcesso(null);
            }
            permissaoService.removerPermissao(permissao);
        }
        return "redirect:/permissao/paginar/1/15";
    }

}
/*                    End of Class                                            */