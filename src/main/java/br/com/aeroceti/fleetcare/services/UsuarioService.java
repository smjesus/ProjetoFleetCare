/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.services;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.aeroceti.fleetcare.model.dto.UsuarioDTO;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import br.com.aeroceti.fleetcare.model.user.UsuarioLogin;
import br.com.aeroceti.fleetcare.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.aeroceti.fleetcare.model.dto.MensagemDTO;
import br.com.aeroceti.fleetcare.model.user.NivelAcesso;
import br.com.aeroceti.fleetcare.model.user.UsuarioVerificador;
import br.com.aeroceti.fleetcare.repositories.PermissoesRepository;
import br.com.aeroceti.fleetcare.repositories.UsuarioVerificadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de SERVICOS para o objeto Usuario (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class UsuarioService  implements UserDetailsService {

    @Autowired
    private PasswordEncoder   passwdEncoder ;
    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private PermissoesRepository nivelRepository;
    @Autowired
    private UsuarioVerificadorRepository  uuidRepository;
    
    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    /**
     * Metodo para o SpringSecurity carregar o usuario logado.
     * 
     * @param email  -  nome de usuario que realizou o login
     * @return       -  registra um UserDetails
     * @throws UsernameNotFoundException 
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario userLogin = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        
        return new UsuarioLogin(userLogin);
    }

    /**
     * Listagem de TODOS os usuarios cadastrados no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o resultado da pesquisa
     * @return ArrayList com varios objetos USUARIO
     */
    public List<Usuario> listar(boolean ordenarByNome) {
        List<Usuario> listagem;
        if (ordenarByNome) {
            logger.info("Obtendo uma listagem ORDENADA de todos os usuarios...");
            listagem = userRepository.findByOrderByNomeAsc();
        } else {
            logger.info("Obtendo uma listagem GENERICA de todos os usuarios...");
            listagem = userRepository.findAll();
        }
        for (Usuario user : listagem) { user.getNivelAcesso();   }
        return listagem;
    }
    
    /**
     * Listagem PAGINADA de todos os Usuarios cadastrados no Banco de dados.
     * 
     * @param page     - pagina atual da requisicao
     * @param pageSize - tamanho de itens para apresentar na pagina
     * @return         - devolve pra View um objeto Pageable
     */
    public Page<Usuario> paginar(int page, int pageSize){ 
        Pageable pageRequest = PageRequest.of((page -1), pageSize);
        Page<Usuario> paginas = userRepository.findByOrderByNomeAsc(pageRequest);
        return paginas; 
    }

    /**
     * Busca um Colaborador pelo ID que esta cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo o Colaborador encontrado (se houver)
     */
    public Optional<Usuario> buscar(Long identidade) {
        logger.info("Obtendo Colaborador pelo ID: " + identidade);
        return userRepository.findByEntidadeID(identidade);
    }
    
    /**
     * Busca um Colaborador pelo CPF fornecido.
     *
     * @param cpf - CPF do usuario desejado
     * @return ResponseEntity - Mensagem de Erro ou Objeto Usuario
     */
    public Optional<Usuario> selecionarCPF(String cpf) {
        logger.info("Obtendo um usuario com o CPF " + cpf);
        return userRepository.findByCpf(cpf);
    }

    /**
     * Busca um Colaborador pelo E-MAIL fornecido.
     *
     * @param email - E-mail do usuario desejado
     * @return ResponseEntity - Mensagem de Erro ou Objeto Usuario
     */
    public Optional<Usuario> selecionarEmail(String email) {
        logger.info("Obtendo um usuario com o E-Mail " + email);
        return userRepository.findByEmail(email);
    }

    /**
     * Busca um VERIFICADOR na base de dados que pertenca ao usuario = id
     * 
     * @param identidade - ID do Usuario que se deseja o Verificador
     * @return Objeto Optional com o Verificador encontrado ou vazio
     */
    public Optional<UsuarioVerificador> buscarVerificador(Long identidade) {
        logger.info("Obtendo o Verificador de um Colaborador pelo ID: " + identidade);
        return uuidRepository.findByUsuarioId(identidade);
    }
    
    public Optional<UsuarioVerificador> buscarVerificadorByUUID(UUID identidade) {
        logger.info("Obtendo o Verificador de um Colaborador pelo UUID: " + identidade);
        return uuidRepository.findBycodigoUUID(identidade);
    }
    
    public ResponseEntity<?> atualizarVerificador(UsuarioVerificador verificador) {
        logger.info("Verificador atualizado no banco de dados!");
        return new ResponseEntity<>(uuidRepository.save(verificador), HttpStatus.OK);
    }
    
    /**
     * Deleta um VERIFICADOR da base de dados
     * 
     *Um Verificador deve ser deletado apos a verificacao da conta, ou quando o
     * usuario for deletado do sistema.
     *
     * @param verificador - Verificador que se deseja excluir
     * @return  ResponseEntity com uma mensagem de sucesso
     */
    public ResponseEntity<?> deletarVerificador(UsuarioVerificador verificador) {
        // ATUALIZA o objeto do banco de dados
        verificador.setUsuario(null);
        uuidRepository.delete(verificador);
        logger.info("Verificador deletado do banco de dados!");
        return new ResponseEntity<>("Verificador deletado do banco de dados!", HttpStatus.OK);
    }
    
    /**
     * Metodo para cadastrar um Usuario na base de dados.
     *
     * @param user - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> cadastrar(Usuario user) {
        // Criptografa a senha:
        String senhaCriptografada =  passwdEncoder.encode(user.getPassword()).trim();
        user.setPassword(senhaCriptografada );
        user.setConfirmarSenha(senhaCriptografada);        

        // Salva o Usuario:
        userRepository.save(user);        
        
        // finaliza devolvendo <mensagem> via ResponsEntity:
        logger.info("Usuario " + user.getNome() + "(" + user.getCpf() + ")" + " salvo no banco de dados!");
        return new ResponseEntity<>(new MensagemDTO("Usuario " + user.getNome() + "(" + user.getCpf() + ")" + " salvo no banco de dados!" ), HttpStatus.CREATED);
    }
    
    /**
     * Metodo para atualizar um Usuario na base de dados.
     *
     * @param user - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> atualizar(Usuario user) {
        logger.info("Atualizando o usuario (" + user.getEntidadeID() + ") no banco de dados...");
        // ATUALIZA o objeto do banco de dados
        userRepository.save(user);
        logger.info("Usuario " + user.toString() + " ATUALIZADO no banco de dados!");
        return new ResponseEntity<>(new MensagemDTO("Usuario " + user.getNome() + "(" + user.getCpf() + ")" + " ATUALIZADO no banco de dados!" ), HttpStatus.OK);
    }

    /**
     * Atualiza o STATUS do usuario, ou DELETA um usuario.
     *
     * @param usuarioID - UsuarioID do usuario desejado
     * @param status    - Se 0 (ZERO) Desativa, se 1 (UM) Ativa, se 2 (DOIS) DELETA
     * @return ResponseEntity - Mensagem de Confirmacao/Erro ou Objeto Usuario
     */
    public ResponseEntity<?> arquivar(Long usuarioID, int status) {
        Usuario usuario;
        String resposta = "";
        logger.info("Obtendo o usuario do banco para modificar STATUS ...");
        
        Optional<Usuario> usuarioSolicitado = userRepository.findById(usuarioID);
        if( usuarioSolicitado.isPresent() ) {
            usuario = usuarioSolicitado.get();
            switch (status) {
                case 0 -> {
                    // DESATIVA o usuario
                    usuario.setAtivo(false);
                    userRepository.save(usuario);
                    logger.info("Usuario " + usuario.getNome() + " DESATIVADO no Sistema!");
                    resposta = "Usuario " + usuario.getNome() + " DESATIVADO no Sistema!"; 
                }
                case 1 -> {
                    // ATIVA o usuario
                    usuario.setAtivo(true);
                    userRepository.save(usuario);
                    logger.info("Usuario " + usuario.getNome() + " ATIVADO no Sistema!");
                    resposta = "Usuario " + usuario.getNome() + " ATIVADO no Sistema!"; 
                }
                case 2 -> {
                    // DELETA O USUARIO
                    userRepository.delete(usuario);
                    logger.info("Usuario " + usuario.getNome() + " DELETADO do Sistema!");
                    resposta = "Usuario " + usuario.getNome() + " DELETADO do Sistema!"; 
                }
            }
        } else {
            logger.info("Nenhum registro encontrado com o ID informado!");
            return new ResponseEntity<>(new MensagemDTO("Nenhum registro encontrado com o ID informado!"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new MensagemDTO(resposta) , HttpStatus.OK);        
    }

    //--------------------------------------------------------------------------------------//
    /** - - - - - - - - - - - - METODOS COMPLEMENTARES - - - - - - - - - - - - - - - - - -  */
    //--------------------------------------------------------------------------------------//
    
    private boolean isCPFPresent(UsuarioDTO user) {
        // Verifica se CPF ja cadastrado:
        Optional<Usuario> usuarioSolicitado = userRepository.findByCpf(user.cpf());
        if (usuarioSolicitado.isPresent()) {
            logger.info("Dados nao cadastrados: Usuario já está cadastrado!");
            return true;
        }
        return false;
    }

    private boolean isValidEmail(String contaEmail) {
        // Verifica se email valido:
        if ( contaEmail != null && contaEmail.length() > 0 ) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(contaEmail);
            if (matcher.matches()) {
                // conta valida
                return true;
            } else {
                // conta invalida
                logger.info("Dados nao cadastrados: E-MAIL precisa ser VALIDO!");
            }
        }
        return false;
    }

    public Optional<NivelAcesso> buscarPermissao(NivelAcesso nivelAcesso) {
        return nivelRepository.findByEntidadeID( nivelAcesso.getEntidadeID() );
    }
    
}
/*                    End of Class                                            */