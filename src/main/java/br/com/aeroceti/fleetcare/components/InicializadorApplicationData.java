/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import br.com.aeroceti.fleetcare.model.user.NivelAcesso;
import br.com.aeroceti.fleetcare.model.user.Usuario;
import br.com.aeroceti.fleetcare.repositories.PermissoesRepository;
import br.com.aeroceti.fleetcare.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Este Componente inicializa um usuario Administrador e a Permissao respectiva.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Component
public class InicializadorApplicationData implements CommandLineRunner {
    
    @Autowired
    private PermissoesRepository rulesRepository;
    @Autowired
    private UsuarioRepository    userRepository;
    @Autowired
    private PasswordEncoder      passwdEncoder ;
    
    private final Logger logger = LoggerFactory.getLogger(InicializadorApplicationData.class);
    
    /**
     *  Metodo principal que inicializa um usuario padrao.
     * 
     * @param args - argumentos passados para a aplicacao
     * @throws java.lang.Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Usuario userAdmin = new Usuario();
        NivelAcesso   permissao = new NivelAcesso(null,"Administrador");
        NivelAcesso   gerente   = new NivelAcesso(null, "Proprietario");
        
        // Verifica se o Perfil Administrador existe no BD:
        int valor = rulesRepository.countByNome("Administrador");
        if( valor == 0 ) {
            // senao existe, cria um no BD:
            rulesRepository.save(permissao);
            logger.info("Sistema FleetCare Inicializando (Criada permissao de Admin) ... ");
        }
        // Verifica se o Perfil Proprietario existe no BD:
        valor = rulesRepository.countByNome("Proprietario");
        if( valor == 0 ) {
            // senao existe, cria um no BD:
            rulesRepository.save(gerente);
            logger.info("Sistema FleetCare inicializando (Criada permissao de Proprietario) ... ");
        }
        // Verifica se há administrador cadastrado:
        Optional<NivelAcesso> permissaoSolicitada = rulesRepository.findByNomeAndUsuarios(permissao.getNome());
        permissao = permissaoSolicitada.get();
        if( permissao.getUsuarios().isEmpty() ) {
            // Nao tem administrador, cadastrando um 'default':
            Optional<Usuario> userSolicitado = userRepository.findByCpf("53376207704");
            if( userSolicitado.isEmpty() ) {
                userAdmin.setCpf("53376207704"); userAdmin.setDataNascimento("01/01/2024");
                userAdmin.setNome("Administrador");userAdmin.setSobrenome("do FleetCare");
                userAdmin.setEmail("admin@fleetcare.com"); userAdmin.setPassword(passwdEncoder.encode("admin-feetcare"));
                logger.info("Sistema FleetCare Inicializando (Criado Admin default) ... ");
            } else {
                userAdmin = userSolicitado.get();
            }
            userAdmin.setAtivo(true);
            userAdmin.setNivelAcesso(permissao);
            userRepository.save(userAdmin);
            logger.info("Sistema FleetCare Inicializando (Admin default configurado)!");
        }
        
        logger.info("Sistema FleetCare Inicializado. (Runner concluido)");
    }
    
}
/*                    End of Class                                            */