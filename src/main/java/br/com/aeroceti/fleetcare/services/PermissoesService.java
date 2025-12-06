/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.services;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import br.com.aeroceti.fleetcare.model.user.NivelAcesso;
import br.com.aeroceti.fleetcare.model.dto.MensagemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.aeroceti.fleetcare.repositories.PermissoesRepository;

/**
 * Classe de SERVICOS para o objeto NivelAcesso (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class PermissoesService {

    @Autowired
    private PermissoesRepository rulesRepository;

    private final Logger logger = LoggerFactory.getLogger(PermissoesService.class);

    /**
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o resultado da pesquisa
     * @return ArrayList em JSON com varios objetos USUARIO
     */
    public List<NivelAcesso> listar(boolean ordenarByNome) {
        logger.info("Obtendo uma listagem de todas as permissoes...");
        if (ordenarByNome) {
            logger.info("Executado Servico de listagem ordenada das permissoes ...");
            return rulesRepository.findByOrderByNomeAsc();
        }
        logger.info("Executado Servico de listagem sem ordenacao das permissoes ...");
        return rulesRepository.findAll();
    }
    
    /**
     * Listagem PAGINADA de todos as Permissoes cadastradas no Banco de dados.
     * 
     * @param page     - pagina atual da requisicao
     * @param pageSize - tamanho de itens para apresentar na pagina
     * @return         - devolve pra View um objeto Pageable
     */
    public Page<NivelAcesso> paginar(int page, int pageSize){ 
        Pageable pageRequest = PageRequest.of((page -1), pageSize);
        Page<NivelAcesso> paginas = rulesRepository.findByOrderByNomeAsc(pageRequest);
        // Força carregamento dos relacionamentos desejados
        paginas.forEach(c -> {
            c.getUsuarios().size();    // inicializa Colaboradores
        });

        return paginas; 
    }
    
    /**
     * Verifica se um NOME de PERMISSAO ja esta cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return Boolean - TRUE se o nome informado existe no banco; e FALSE caso contrario
     */
    public boolean isPermissaoCadastrada(String nome) {
        return ( rulesRepository.countByNome(nome) > 0 );
    }

    /**
     * Busca uma PERMISSAO pelo ID cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<NivelAcesso> buscarPermissao(Long identidade) {
        return rulesRepository.findByEntidadeID(identidade);
    }
    
    /**
     * Busca uma PERMISSAO pelo NOME cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<NivelAcesso> buscarPermissaoPorNome(String nome) {
        return rulesRepository.findByNome(nome);
    }

    /**
     * Metodo para cadastrar uma Permissao na base de dados.
     *
     * @param permissao - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> salvar(NivelAcesso permissao) {
        logger.info("Persistindo permissao no banco de dados...");
        rulesRepository.save(permissao);
        logger.info("Permissao " + permissao.getNome() + " salva no banco de dados!");
        return new ResponseEntity<>( permissao, HttpStatus.CREATED);
    }
 
    /**
     * Metodo para atualizar uma Permissao na base de dados.
     *
     * @param permissao - Objeto Usuario com os dados a serem atualizados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> atualizar(NivelAcesso permissao) {
        // ATUALIZA o objeto do banco de dados
        logger.info("Permissao " + permissao.getNome() + " atualizada no banco de dados!");
        return new ResponseEntity<>(rulesRepository.save(permissao), HttpStatus.OK);
    }
    
    /**
     * DELETA uma permissao do banco de dados.
     *
     * @param permissao - Permissao a ser deletada
     * @return ResponseEntity - Mensagem de Erro ou Sucesso na operacao
     */
    public ResponseEntity<?> removerPermissao(NivelAcesso permissao) {
        logger.info("Excluindo permissao do banco ...");
        rulesRepository.delete(permissao);
        logger.info("Requisicao executada: Permissao DELETADA no Sistema!");
        return new ResponseEntity<>(new MensagemDTO("Permissao DELETADA no Sistema!"), HttpStatus.OK);
    }
    
}
/*                    End of Class                                            */