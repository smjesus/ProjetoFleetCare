/**
 * Projeto:  CachaBot - BOT para o Discord para gerenciar Usuarios.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.fleetcare.services;

import br.com.aeroceti.fleetcare.model.dto.EmailMessageDTO;
import java.util.Objects;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;
import java.nio.charset.StandardCharsets;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * Classe de SERVICOS para envio de Emails
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class MailSenderService {
    
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private final String defaultFrom;
    
    public MailSenderService(JavaMailSender sender, SpringTemplateEngine templateEngine, @Value("${spring.mail.username}") String defaultFrom) {
        this.mailSender = sender;
        this.templateEngine = templateEngine;
        this.defaultFrom = defaultFrom;
    }
    
    /**
     * Envia um e-mail HTML simples.
     * 
     * @param message DTO contendo os dados para o email
     * @throws MessagingException em caso de erro no envio
     */
    public void sendHtmlEmail(EmailMessageDTO message) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(message.to());
        helper.setSubject(message.subject());
        helper.setFrom(
            message.from() != null && !message.from().isBlank() 
                ? message.from() 
                : defaultFrom
        );
        helper.setText(message.textBody(), true); // true -> HTML
        mailSender.send(mime);
    }

    /**
     * Envia um e-mail HTML usando template Thymeleaf.
     * 
     * Templates ficam em src/main/resources/templates/ (ex: templates/email/welcome.html)
     * @param templateName nome do template (ex: "email/welcome")
     * @param message EmailMessageDTO DTO com os dados do email
     * @param model Dados do template (objeto de domínio, pode ser o próprio record)
     * @param baseUrl
     * @param token
     * @throws MessagingException em caso de erro
     */
    public void sendTemplateEmail(EmailMessageDTO message, String templateName, Object model, String baseUrl, String token) throws MessagingException {
        Context context = new Context();
        // adiciona o model como "email" no template
        context.setVariable("email",   message);
        context.setVariable("model",     model);
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("link", baseUrl+"/usuario/uuid/" + token);

        String html = templateEngine.process(templateName, context);

        // reutiliza o método padrão
        sendHtmlEmail(new EmailMessageDTO(
                message.to(),
                message.from(),
                message.nomeUsuario(),
                message.subject(),
                html
        ));
    }

    /**
     * Envia e-mail HTML com anexos (InputStreamSource permite anexar arquivos em memória).
     * @param to destinatário
     * @param subject assunto
     * @param htmlBody corpo em HTML
     * @param attachmentName nome do anexo
     * @param attachmentSource fonte do anexo
     * @throws MessagingException em caso de erro
     */
    public void sendHtmlEmailWithAttachment(String to, String subject, String htmlBody, String attachmentName, InputStreamSource attachmentSource) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        if (defaultFrom != null && !defaultFrom.isBlank()) helper.setFrom(defaultFrom);
        helper.setText(htmlBody, true);
        helper.addAttachment(Objects.requireNonNull(attachmentName), attachmentSource);
        mailSender.send(message);
    }
    
}
/*                    End of Class                                            */