/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.controllers.v1.web;

import br.com.aeroceti.fleetcare.services.I18nService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller para a apresentacao da pagina de erro.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
public class WebErrorController implements ErrorController {

    @Autowired
    private I18nService i18svc;
    
    Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @GetMapping("/error")
    public ModelAndView handleError (HttpServletRequest request, Model model, Locale locale) {
        List<String> rotas = Arrays.asList("/usuario", "/carro", "/fabricante", "/modelo", "/permissao", "/dashboard");
        var mensagem = i18svc.buscarMensagem("error.all.page", locale);
        
        Object httpStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        if(httpStatus != null){
            int code = Integer.parseInt(httpStatus.toString());
            // Erros de acesso negado:
            if( code == 403 ) {
                logger.info("Configurando mensagem 403 ...");
                mensagem = i18svc.buscarMensagem("error.403.page", locale);
            }            
            // Erros de solicitacao invalida:
            if( (code >399 && code < 499) && (code != 403) ) {
                logger.info("Configurando mensagem de Solicitacao Invalida ...");
                mensagem = i18svc.buscarMensagem("error.400.page", locale);
            }
            // Erros de processamento:
            if( code >499 && code < 599){
                logger.info("Configurando mensagem Erro Interno ...");
                mensagem = i18svc.buscarMensagem("error.500.page", locale);
            }
        }
        model.addAttribute("errorPage", mensagem);
        mensagem = rotas.stream().anyMatch(uri.toString()::contains) ? "Dashboard" : "Public";
        model.addAttribute("errorOrigem", mensagem);
                
        logger.info("Encaminhando para nova pagina de erro ...");
        return new ModelAndView("error");
    }
    
}
/*                    End of Class                                            */