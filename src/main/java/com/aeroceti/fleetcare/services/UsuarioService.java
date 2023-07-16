/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.services;

import com.aeroceti.fleetcare.controllers.UsuarioController;
import com.aeroceti.fleetcare.dataTransfers.MensagemDTO;
import com.aeroceti.fleetcare.model.Usuario;
import com.aeroceti.fleetcare.repositories.UsuarioRepository;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Classe de SERVICOS para o objeto Usuario (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class UsuarioService {

    @Autowired
    private MensagemDTO mensagem;
    @Autowired
    private PasswordEncoder codePass;
    @Autowired
    private UsuarioRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    /**
     * Metodo para cadastrar um Usuario na base de dados.
     *
     * @param user - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario
     * cadastrado
     */
    public ResponseEntity<?> cadastrar(Usuario user) {
        logger.info("Validando os dados para persistir usuario no banco de dados...");
        if (user.getNome().equals("")) {
            mensagem.setMensagem("NOME precisa ser preenchido!");
            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        } else if (user.getEmail() != null && user.getEmail().length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                mensagem.setMensagem("E-MAIL precisa ser VALIDO!");
                logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
                return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
            }
        }
        logger.info("Usuario validado, preparando ID e codificando a senha...");
        user.setUsuarioID(null);
        // Codifica a senha do usuario no banco de dados:
        user.setPassword(codePass.encode(user.getPassword()));
        logger.info("Usuario " + user.getNome() + " salvo no banco de dados!");
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    /**
     * Metodo para cadastrar um Usuario na base de dados.
     *
     * @param user - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario
     * cadastrado
     */
    public ResponseEntity<?> atualizar(Usuario user) {
        Usuario userAtual;
        logger.info("Obtendo o usuario do banco para atualizar...");
        // Valida se existe usuario com ID fornecido
        if (userRepository.countByUsuarioID(user.getUsuarioID()) == 0
                || userRepository.countByUsuarioID(user.getUsuarioID()) > 1) {
            mensagem.setMensagem("Nao existe usuario com o ID informado!");
            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        } else {
            // obtem o objeto PERSISTIDO
            userAtual = userRepository.findByUsuarioID(user.getUsuarioID());
        }
        // Valida campo nome (deve estar preenchido) 
        if (user.getNome().equals("")) {
            mensagem.setMensagem("NOME precisa ser PREENCHIDO!");
            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        //Valida o email informado
        if (user.getEmail() != null && user.getEmail().length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                mensagem.setMensagem("E-MAIL precisa ser VALIDO!");
                logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
                return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
            }
        } else {
            mensagem.setMensagem("E-MAIL precisa ser INFORMADO!");
            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        logger.info("Usuario validado, codificando a senha SE necessario ...");
        // Codifica a senha do usuario no banco de dados:
        if (!user.getPassword().equals("")) {
            userAtual.setPassword(codePass.encode(user.getPassword()));
        }
        // ATUALIZA o objeto do banco de dados
        userAtual.setCpf(user.getCpf());
        userAtual.setDataNascimento(user.getDataNascimento());
        userAtual.setEmail(user.getEmail());
        userAtual.setNome(user.getNome());
        userAtual.setWhatsapp(user.getWhatsapp());
        userAtual.setSexo(user.getSexo());
        logger.info("Usuario " + user.getNome() + " salvo no banco de dados!");
        return new ResponseEntity<>(userRepository.save(userAtual), HttpStatus.OK);
    }

    public ResponseEntity<?> arquivar(UUID usuarioID, int status) {
        Usuario userAtual;
        logger.info("Obtendo o usuario do banco para ARQUIVAR...");
        // Valida se existe usuario com ID fornecido
        if (userRepository.countByUsuarioID(usuarioID) == 0) {
            mensagem.setMensagem("Nao existe usuario com o ID informado!");
            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        } else {
            // obtem o objeto PERSISTIDO
            userAtual = userRepository.findByUsuarioID(usuarioID);
        }
        switch (status) {
            case 0 -> {
                // ARQUIVA o usuario
                userAtual.setAtivo(false);
                userRepository.save(userAtual);
                mensagem.setMensagem("Usuario ARQUIVADO no Sistema!");
            }
            case 1 -> {
                // ATIVA o usuario
                userAtual.setAtivo(true);
                userRepository.save(userAtual);
                mensagem.setMensagem("Usuario ATIVADO no Sistema!");
            }
            case 2 -> {
                // DELETA O USUARIO
                userRepository.delete(userAtual);
                mensagem.setMensagem("Usuario DELETADO do Sistema!");
            }
        }
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    /**
     * Listagem em ordem ALFABETICA dos NOMES de TODOS os usuarios cadastrados
     * no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o
     * resultado da pesquisa
     * @return ArrayList em JSON com varios objetos USUARIO
     */
    public ResponseEntity<?> listar(boolean ordenarByNome) {
        logger.info("Obtendo uma listagem de todos os usuarios...");
        if (ordenarByNome) {
            return new ResponseEntity<>(userRepository.findByOrderByNomeAsc(), HttpStatus.OK);
        }
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Busca um usuario pelo email fornecido.
     *
     * @param email - E-Mail do usuario desejado
     * @return ResponseEntity - Mensagem de Erro ou Objeto Usuario
     */
    public ResponseEntity<?> selecionar(String email) {
        logger.info("Obtendo um usuario com o email " + email);
        if (userRepository.countByEmail(email) == 0) {
            mensagem.setMensagem("Nenhum registro encontrado com o email informado!");
            logger.info("Falha na procura: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.countByEmail(email) > 1) {
            mensagem.setMensagem("Foram encontrados mais de um Usuario com o email informado!");
            logger.info("Falha na procura: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);
    }

    /**
     * Busca um usuario pelo ID fornecido.
     *
     * @param userID - UsuarioID do usuario desejado
     * @return ResponseEntity - Mensagem de Erro ou Objeto Usuario
     */
    public ResponseEntity<?> selecionar(UUID userID) {
        logger.info("Obtendo um usuario com o ID " + userID.toString());
        if (userRepository.countByUsuarioID(userID) == 0) {
            mensagem.setMensagem("Nenhum registro encontrado com o ID informado!");
            logger.info("Falha na procura: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.countByUsuarioID(userID) > 1) {
            mensagem.setMensagem("Foram encontrados mais de um Usuario com o ID informado!");
            logger.info("Falha na procura: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userRepository.findByUsuarioID(userID), HttpStatus.OK);
    }

    /**
     * Validacao da senha do usuario para acesso ao sistema
     *
     * @param senha - Senha do usuario
     * @param login - Email (login) do usuario
     * @return ResponseEntity - Mensagem de Erro ou Objeto Usuario (usuario
     * logado)
     */
    public ResponseEntity<?> validarSenha(String senha, String login) {
        logger.info("Validando a senha do usuario ... ");
        Usuario user = userRepository.findByEmail(login);
        try {
            boolean situacao = codePass.matches(senha, user.getPassword());

            if (!situacao) {
                mensagem.setMensagem("Validacao incorreta: Senha nao confere!");
                logger.info("Falha no Login: " + mensagem.getMensagem());
                return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            mensagem.setMensagem("Validacao incorreta: Confirme o email informado!");
            logger.info("Falha no Login: " + mensagem.getMensagem());
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
