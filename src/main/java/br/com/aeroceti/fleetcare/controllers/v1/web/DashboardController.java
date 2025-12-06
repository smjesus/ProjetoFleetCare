/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.web;

import br.com.aeroceti.fleetcare.model.user.Usuario;
import org.slf4j.Logger;
import java.util.Locale;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import br.com.aeroceti.fleetcare.services.I18nService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller para a apresentacao do Dashboard do sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
public class DashboardController {

    @Autowired
    private I18nService i18svc;
    
    Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    /*--------------------------------------------------------------------------
     *                 PAGINAS DE ACESSO PUBLICO
     *--------------------------------------------------------------------------
     */

    @GetMapping("/")
    public String getHomePage(Model model){
        logger.info("Redirecionando view para Pagina Inicial...");
        return "index";
    }   
    
    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model, Locale locale){
        if (error != null) {
            model.addAttribute("mensagemErro", i18svc.buscarMensagem("login.errorMessage", locale) );
        }
        logger.info("Redirecionando view para pagina de Login...");
        return "login";
    }

    @GetMapping("/novaconta")
    public String getNovaConta(Model modelo){
        modelo.addAttribute("usuario", new Usuario());
        logger.info("Apresentando Formulario para cadastro de nova conta...");
        return "/model/usuario/cadastro";
    }

    /*--------------------------------------------------------------------------
     *                 PAGINAS DE ACESSO RESTRITO
     *--------------------------------------------------------------------------
     */

    @GetMapping("/dashboard")
    public String getDashboard(Model model){
        logger.info("Redirecionando view para o Dashboard ...");
        return "dashboard";
    }
  
    @GetMapping("/api-doc")
    public void redirectSwagger(HttpServletRequest request, HttpServletResponse response) {
        String baseUrl = request.getRequestURL().toString()
            .replace(request.getRequestURI(), request.getContextPath());         
        String url = baseUrl + "/swagger-ui/index.html"; 
        response.setHeader("Location",url);
        response.setStatus(302);
    }
    
}
/*                    End of Class                                            */