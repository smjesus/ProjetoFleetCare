/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.web;

import java.util.UUID;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import br.com.aeroceti.fleetcare.model.dto.EmailMessageDTO;
import br.com.aeroceti.fleetcare.model.user.NivelAcesso;
import br.com.aeroceti.fleetcare.model.user.UsuarioVerificador;
import br.com.aeroceti.fleetcare.services.I18nService;
import br.com.aeroceti.fleetcare.services.MailSenderService;
import br.com.aeroceti.fleetcare.services.PermissoesService;
import br.com.aeroceti.fleetcare.services.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Classe Controller para o objeto Usuario.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/usuario/")
public class UsuarioController {

    @Autowired
    private I18nService i18svc;
    @Autowired
    private UsuarioService userService;
    @Autowired
    private MailSenderService mailService;
    @Autowired
    private PermissoesService profileService;
    
    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    /**
     * Listagem de TODOS os USUARIOS cadastrados no Banco de dados.
     * 
     * Caso desejar ordenar por nome em ordem alfabetica, 
     * passar o valor TRUE senao FALSE
     *
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */
    @RequestMapping("/listar/{ordenar}")
    public String listagem(Model modelo, @PathVariable boolean ordenar) {
        logger.info("Listando todos os usuarios do sistema ...");
        modelo.addAttribute("colecao",userService.listar(ordenar));
        return"/model/usuario/listagem";
    }

    /**
     * Listagem de TODOS os USUARIOS cadastrados no Banco de dados (PAGINADA).
     * 
     * @param modelo
     * @param page     numero da pagina em exibicao
     * @param pageSize total de itens na pagina
     * @return ModelAndView preparada pelo Thymeleaf com o objeto para listagem.
     */
    @RequestMapping("/paginar/{page}/{pageSize}")
    public String listar( Model modelo, @PathVariable int page, @PathVariable int pageSize ) {
        logger.info("Recebida requisicao para listar os Usuários com PAGINACAO ...");
        modelo.addAttribute("colecao",userService.paginar(page, pageSize));
        return "/model/usuario/usuarios";
    }
        
    /**
     * FORMULARIO para cadastrar um Usuario na base de dados.
     *
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um usuario.
     * Propriedades obrigatorias: Nome, E-mail e CPF (valores validos).
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */
    @GetMapping("/cadastrar")
    public String cadastrarUsuario(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        modelo.addAttribute("permissoesList",profileService.listar(true));
        logger.info("Requisicao no UserController para cadastro:  encaminhando ao formulario ...");
        return "/model/usuario/cadastro";
    }

    /**
     * Metodo para gravar um Usuario na base de dados.
     * Esta funcao envia os dados do Cadastro (Formulario) de um usuario para o banco de dados.
     * 
     * @param usuario - objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * @param locale  -  idioma i18n
     * @param modelView
     * @param request
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */  
    @Transactional
    @PostMapping("/gravar")
    public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes atributes, Locale locale, Model modelView, HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString()
            .replace(request.getRequestURI(), request.getContextPath());        
        logger.info("Requisicao para gravar um cadastro preenchido pelo Administrador...");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("Dados incorretos. Voltando para o formulario.");
            return "/model/usuario/cadastro" ;
        }
        try {
            // Salva o usuario no banco:
            processarCadastro(usuario, modelView, baseUrl);
        } catch(MessagingException ex) {
            logger.info( "Erro na gravação dos dados: {}", ex.getMessage() );
            atributes.addFlashAttribute("mensagem", i18svc.buscarMensagem("usuario.gravar.falha", locale));
            return "/model/usuario/cadastro" ;
        }
        return "redirect:/usuario/paginar/1/15";
    }

    @Transactional
    @PostMapping("/novaconta")
    public String cadastrarConta(@Valid Usuario usuario, BindingResult result, RedirectAttributes atributes, Locale locale, Model modelView, HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString()
            .replace(request.getRequestURI(), request.getContextPath());        
        logger.info("Requisicao para gravar um cadastro solicitado pelo usuario...");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("Dados incorretos. Voltando para o formulario.");
            return "/model/usuario/cadastro" ;
        }
        try {
            // Salva o usuario no banco:
            processarCadastro(usuario, modelView, baseUrl);
        } catch(MessagingException ex) {
            logger.info( "Erro na gravação dos dados: {}", ex.getMessage() );
            atributes.addFlashAttribute("mensagem", i18svc.buscarMensagem("usuario.gravar.falha", locale));
            return "/model/usuario/cadastro" ;
        }
        // Usa a pagina de Erro para apresentar uma mensagem de sucesso ao usuario:
        modelView.addAttribute("errorType", "SUCESSO");
        modelView.addAttribute("errorPage1", i18svc.buscarMensagem("home.web.novaconta.1", locale));
        modelView.addAttribute("errorPage2", i18svc.buscarMensagem("home.web.novaconta.2", locale));
        modelView.addAttribute("errorOrigem", "Public");
        return "error";
    }

    private void processarCadastro(Usuario usuario, Model modelView, String baseUrl) throws MessagingException {
        usuario.setEntidadeID(null);
        usuario.setAtivo(false);
        // atualiza o nivel
        if (usuario.getNivelAcesso() != null) {
            Optional<NivelAcesso> nivel = userService.buscarPermissao(usuario.getNivelAcesso());
            if( nivel.isPresent() )
                usuario.setNivelAcesso(nivel.get());
        } else {
            usuario.setNivelAcesso(null);
        }
        userService.cadastrar(usuario);
        // cria o token para validar a conta:
        UsuarioVerificador verificador;
        logger.info("Processando o Token para {} ... ", usuario.toString() );
        Optional<UsuarioVerificador> userVerificador = userService.buscarVerificador(usuario.getEntidadeID());
        if( userVerificador.isPresent() ) {
            logger.info("Encontrou um Token para Ativar a conta do {}, validando por mais 25 minutos... ", usuario.getNome() );
            verificador = userVerificador.get();
        } else {
            logger.info("Criando um novo Token para o usuario ({}) validar sua conta... ", usuario.getNome() );
            verificador = new UsuarioVerificador();
            verificador.setVerificadorID(null);
        }
        verificador.setUsuario(usuario);
        verificador.setCodigoUUID(UUID.randomUUID());
        verificador.setValidade(Instant.now().plusMillis(1500000));
        userService.atualizarVerificador(verificador);
        // Envia o email para ativar a conta:
        EmailMessageDTO mensagem = new EmailMessageDTO(usuario.getEmail(), null, usuario.getNome(), "Ativação de Conta no FleetCare", "");
        logger.info("Enviando email para ativacao da conta... " );
        mailService.sendTemplateEmail(mensagem, "messages/ativarConta", modelView, baseUrl, verificador.getCodigoUUID().toString().trim());
    }
    
    /**
     * FORMULARIO para atualizar um Usuario da base de dados.
     * 
     * @param id - ID do objeto a ser persistido
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */
    @GetMapping("/atualizar/{id}")
    public String atualizarUsuario( @PathVariable("id") long id, Model modelo ) {
        Optional<Usuario> usuarioSolicitado = userService.buscar(id);
        if( usuarioSolicitado.isPresent() ) {
            Usuario usuario = usuarioSolicitado.get();
            logger.info("Requisicao no UserController: ALTERANDO  os dados do " + usuario.getNome());
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("permissoesList",profileService.listar(true));
        } else {
            logger.info("Requisicao no UserController: ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
            return "redirect:/usuario/paginar/1/15";
        }
        return "/model/usuario/editar"; 
    }

    /**
     * Atualiza um Usuario na Base de Dados.
     * Atualiza um usuario (com ID valido) que esteja na base de dados com os
     * dados recebidos do formulario de edicao.
     * 
     * @param usuario - objeto a ser persistido
     * @param result  - objeto do contexto HTTP
     * @param modelo  - objeto de manipulacao da view pelo Spring
     * @param locale  - objeto de internacionalizacao do Spring
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */
    @PostMapping("/atualizar")
    public String atualizarUsuario(@Valid Usuario usuario, BindingResult result, Model modelo, Locale locale ) {
        if (result.hasErrors()) {
            logger.info("Requisicao no UserController: ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "/model/usuario/editar";
        }
        // atualiza o nivel
        if (usuario.getNivelAcesso() != null) {
            Optional<NivelAcesso> nivel = userService.buscarPermissao(usuario.getNivelAcesso());
            if( nivel.isPresent() ) 
                usuario.setNivelAcesso(nivel.get());
        } else {
            usuario.setNivelAcesso(null);
        }
        logger.info("Requisicao no UserController: ALTERÇÃO: {} ...  ", usuario.toString() );
        userService.atualizar(usuario);
        return "redirect:/usuario/paginar/1/15";
    }
    
    /**
     *  Este metodo ativa uma conta cadastrada por solicitacao do usuario através do 
     *  email de confirmacao recebido.
     * 
     * @param token - Codigo recebido para ativacao da conta
     * @param model - Objeto de contexto do Spring
     * @param locale - Ojeto de internacionalizacao do Spring
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */
    @GetMapping("/uuid/{token}")
    public String ativarContaByUUID(@PathVariable UUID token, Model model, Locale locale) {
        logger.info("Requisição para ativar uma conta com o UUID: {}", token);
        model.addAttribute("errorPage1", i18svc.buscarMensagem("login.ativar.falhou", locale));
        model.addAttribute("errorOrigem", "Public");
        Optional<UsuarioVerificador> solicitado = userService.buscarVerificadorByUUID(token);
        if( solicitado.isPresent() ) {
            UsuarioVerificador verificador = solicitado.get();
            if( verificador.getValidade().compareTo(Instant.now()) < 0 ) {
                // EXPIROU O TOKEN:
                logger.info("Ativacao da Conta - FALHOU!! - Token expirado");
                model.addAttribute("errorType", "NOTFOUND");
            } else {
                // TOKEN VALIDO
                Usuario usuario = userService.buscar( verificador.getUsuario().getEntidadeID() ).get();
                usuario.setAtivo(true);
                userService.atualizar(usuario);
                model.addAttribute("errorType", "SUCESSO");
                model.addAttribute("errorOrigem", "Public");
                model.addAttribute("errorPage1", i18svc.buscarMensagem("login.ativar.conta.1", locale));
                model.addAttribute("errorPage2", i18svc.buscarMensagem("login.ativar.conta.2", locale));
                logger.info("Ativacao da Conta {} - Realizada com Sucesso!",  usuario.getEmail() );
            }
            userService.deletarVerificador(verificador);
        } else {
            logger.info("Ativacao da Conta - FALHOU!! - TOKEN nao encontrado");
            model.addAttribute("errorType", "NOTFOUND");
        }
        return "error";
    }
    
    /**
     * Este metodo envia um email para a ativar a conta do usuario e validar o email.
     * 
     * @param id -  ID do Usuario que se deseja enviar o email da ativacao de conta
     * @param modelView  - Objeto de contexto do Spring
     * @param request  - Objeto de contexto HTTP do Spring
     * @return  String para o Spring realizar o encaminhamento ao dashboard 
     */
    @GetMapping("/ativar/{id}")
    public String ativarConta(@PathVariable("id") long id, Model modelView, HttpServletRequest request){
        logger.info("Servico de Ativacao de Contas ...");
        String baseUrl = request.getRequestURL().toString()
            .replace(request.getRequestURI(), request.getContextPath());

        Optional<Usuario> userSolicitado = userService.buscar(id);
        if( userSolicitado.isPresent() ) {
            logger.info("Encontrou Colaborador com nome: " + userSolicitado.get().getNome());
           
            // cria ou atualiza o token para validar a senha:
            UsuarioVerificador verificador;
            Optional<UsuarioVerificador> userVerificador = userService.buscarVerificador(userSolicitado.get().getEntidadeID());
            if( userVerificador.isPresent() ) {
                logger.info("Encontrou um Token, validando por mais 25 minutos... " );
                verificador = userVerificador.get();
            } else {
                logger.info("Criando um novo Token para o usuario ... " );
                verificador = new UsuarioVerificador();
                verificador.setVerificadorID(null);
            }
            verificador.setUsuario(userSolicitado.get());
            verificador.setCodigoUUID(UUID.randomUUID());
            verificador.setValidade(Instant.now().plusMillis(1500000));
            userService.atualizarVerificador(verificador);
            
            try{ 
                EmailMessageDTO mensagem = new EmailMessageDTO(userSolicitado.get().getEmail(), null, userSolicitado.get().getNome(), "Ativação de Conta no FleetCare", "");
                logger.info("Enviando email para ativacao da conta... " );
                mailService.sendTemplateEmail(mensagem, "messages/ativarConta", modelView, baseUrl, verificador.getCodigoUUID().toString().trim());
            } catch(MessagingException ex) {
                logger.info("Encontrou no envio do email: " + ex.getMessage());
            }
        } else {
            logger.info("Processando requisicao: ALTERÇÃO NÃO REALIZADA - Referencia Invalida! ");
        }
        return "redirect:/usuario/paginar/1/15";

    }

    /**
     * Atualiza as Permissoes e Status de um Usuario da base de dados informados no formulario de atualizacao.
     * 
     * @param id - ID do objeto a ser atualizado
     * @param modelo - Objeto Model para injetar dados na View
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletarUsuario( @PathVariable("id") long id, Model modelo ) {
        logger.info("Requisicao no UserController: DELETANDO usuario com ID={} ", id);
        Optional<UsuarioVerificador> verificador = userService.buscarVerificador(id);
        if (verificador.isPresent()) {
            userService.deletarVerificador(verificador.get());
        }	
        userService.arquivar(id,2);
        return "redirect:/usuario/paginar/1/15";
    }
    
}
/*                    End of Class                                            */