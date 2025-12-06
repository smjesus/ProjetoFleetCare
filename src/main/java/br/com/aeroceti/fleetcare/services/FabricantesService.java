/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import br.com.aeroceti.fleetcare.model.carro.Fabricante;
import br.com.aeroceti.fleetcare.repositories.FabricanteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Classe de SERVICOS para o objeto Fabricante (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class FabricantesService {

    @Autowired
    private FabricanteRepository fabricaRepository;

    private final Logger logger = LoggerFactory.getLogger(FabricantesService.class);

    /**
     * Listagem de TODOS os Fabricantes cadastradas no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o resultado da pesquisa
     * @return ArrayList  com varios objetos Fabricantes
     */
    public List<Fabricante> listar(boolean ordenarByNome) {
        logger.info("Obtendo uma listagem de todas as permissoes...");
        if (ordenarByNome) {
            logger.info("Executado Servico de listagem ordenada das permissoes ...");
            return fabricaRepository.findByOrderByFabricanteNameAsc();
        }
        logger.info("Executado Servico de listagem sem ordenacao das permissoes ...");
        return fabricaRepository.findAll();
    }
    
    /**
     * Listagem PAGINADA de todos as Permissoes cadastradas no Banco de dados.
     * 
     * @param page     - pagina atual da requisicao
     * @param pageSize - tamanho de itens para apresentar na pagina
     * @return         - devolve pra View um objeto Pageable
     */
    public Page<Fabricante> paginar(int page, int pageSize){ 
        Pageable pageRequest = PageRequest.of((page -1), pageSize);
        Page<Fabricante> paginas = fabricaRepository.findByOrderByFabricanteNameAsc(pageRequest);
        // Força carregamento dos relacionamentos desejados
        paginas.forEach(c -> {
            // c.get().size();    // inicializa Colaboradores
        });
        return paginas; 
    }
        
    /**
     * Verifica se um FRABICANTE pelo NOME ja esta cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return Boolean - TRUE se o nome informado existe no banco; e FALSE caso contrario
     */
    public boolean isFabricanteCadastrado(String nome) {
        return ( fabricaRepository.countByFabricanteName(nome) > 0 );
    }

    /**
     * Busca um FRABICANTE pelo ID cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Fabricante> buscarFabricante(Long identidade) {
        return fabricaRepository.findByEntidadeID(identidade);
    }
    
    /**
     * Busca uma FRABICANTE pelo NOME cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Fabricante> buscarFabricantePorNome(String nome) {
        return fabricaRepository.findByFabricanteName(nome);
    }

    /**
     * Metodo para cadastrar um Fabricante na base de dados.
     *
     * @param fabricante - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> salvar(Fabricante fabricante) {
        logger.info("Persistindo fabricante no banco de dados...");
        fabricaRepository.save(fabricante);
        logger.info("Fabricante " + fabricante.getFabricanteName()+ " salvo no banco de dados!");
        return new ResponseEntity<>( fabricante, HttpStatus.CREATED);
    }
 
    //    
//    /**
//     * Metodo para cadastrar um Fabricante na base de dados.
//     *
//     * @param fabrica - Objeto com os dados a serem gravados
//     * @return ResponseEntity contendo uma mensagem de erro OU um objeto cadastrado
//     */
//    public ResponseEntity<?> cadastrar(FabricaDTO fabrica) {
//        Fabricante fabricaEntity; 
//        logger.info("Validando os dados para persistir fabrica no banco de dados...");
//        if (fabrica.fabricanteName().equals("")) {
//            mensagem.setMensagem("NOME da Permissao precisa ser preenchido!");
//            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        Optional<Fabricante> fabricaSolicitada = fabricaRepository.findByFabricanteName(fabrica.fabricanteName());
//        if( fabricaSolicitada.isPresent() ) {
//            mensagem.setMensagem("Fabricante já está cadastrado!");
//            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        logger.info("Fabrica valida, preparando ID ...");
//        fabricaEntity = (Fabricante) mapeador.map(fabrica,Fabricante.class);
//        fabricaEntity.setFabricanteID(null);
//        fabricaRepository.save(fabricaEntity);
//        logger.info("Permissao " + fabricaEntity.getFabricanteName()+ " salva no banco de dados!");
//        return new ResponseEntity<>( fabricaEntity, HttpStatus.CREATED);
//    }
//    
//    /**
//     * Busca um Fabricante pelo NOME fornecido.
//     *
//     * @param nome - Nome da fabrica desejada
//     * @return ResponseEntity - Mensagem de Erro ou Objeto Permissao desejado
//     */
//    public ResponseEntity<?> selecionar(String nome) {
//        logger.info("Obtendo uma fabrica com o nome de " + nome);
//        if (fabricaRepository.countByFabricanteName(nome) == 0) {
//            mensagem.setMensagem("Nenhum registro encontrado com o nome informado!");
//            logger.info("Falha na procura: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        if (fabricaRepository.countByFabricanteName(nome) > 1) {
//            mensagem.setMensagem("Foram encontrados mais de um Fabricante com o nome informado!");
//            logger.info("Falha na procura: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(fabricaRepository.findByFabricanteName(nome), HttpStatus.OK);
//    }
//
//    /**
//     * Busca uma fabrica pelo ID fornecido.
//     *
//     * @param fabricaID - RulesID da fabrica desejada
//     * @return ResponseEntity - Mensagem de Erro ou Objeto Usuario
//     */
//    public ResponseEntity<?> selecionar(Long fabricaID) {
//        logger.info("Obtendo uma fabrica com o ID " + fabricaID.toString());
//        if (fabricaRepository.countByFabricanteID(fabricaID) == 0) {
//            mensagem.setMensagem("Nenhum registro encontrado com o ID informado!");
//            logger.info("Falha na procura: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        if (fabricaRepository.countByFabricanteID(fabricaID) > 1) {
//            mensagem.setMensagem("Foram encontrados mais de um registro com o ID informado!");
//            logger.info("Falha na procura: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(fabricaRepository.findByFabricanteID(fabricaID), HttpStatus.OK);
//    }
//
//    /**
//     * Metodo para atualizar um Fabricante na base de dados.
//     *
//     * @param fabrica - Objeto com os dados a serem atualizados
//     * @return ResponseEntity contendo uma mensagem de erro OU um objeto cadastrado
//     */
//    public ResponseEntity<?> atualizar(FabricaDTO fabrica) {
//        Fabricante fabricaAtual;
//        logger.info("Obtendo a Fabrica do banco para atualizar...");
//        // Valida se existe usuario com ID fornecido
//        if ( fabricaRepository.countByFabricanteID(fabrica.fabricanteID()) == 0 ) {
//            mensagem.setMensagem("Nao existe Fabricante com o ID informado!");
//            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        // Valida campo nome (deve estar preenchido) 
//        if (fabrica.fabricanteName().equals("")) {
//            mensagem.setMensagem("NOME precisa ser PREENCHIDO!");
//            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        // obtem o objeto PERSISTIDO
//        Optional<Fabricante> fabricaSolicitada = fabricaRepository.findById(fabrica.fabricanteID());
//        if( fabricaSolicitada.isEmpty() ) {
//            mensagem.setMensagem("Fabricante nao Encontrado no Banco de Dados!");
//            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        }
//        logger.info("Permissao valida, atualizando-a ...");
//        fabricaAtual = fabricaSolicitada.get();
//        // ATUALIZA o objeto do banco de dados
//        fabricaAtual.setFabricanteName(fabrica.fabricanteName());
//        logger.info("Fabricante " + fabricaAtual.getFabricanteName()+ " atualizada no banco de dados!");
//        return new ResponseEntity<>(fabricaRepository.save(fabricaAtual), HttpStatus.OK);
//    }
//
//    /**
//     * DELETA uma fabrica do banco de dados.
//     *
//     * @param fabricaID - ID da fabrica a ser deletada
//     * @return ResponseEntity - Mensagem de Erro ou Sucesso na operacao
//     */
//    public ResponseEntity<?> removerFabricante(Long fabricaID) {
//        Fabricante fabricaInformada;
//        logger.info("Obtendo a fabrica do banco para DELETAR ...");
//        // Valida se existe usuario com ID fornecido
//        if (fabricaRepository.countByFabricanteID(fabricaID) == 0) {
//            mensagem.setMensagem("Nao existe Fabricante com o ID informado!");
//            logger.info("Dados nao cadastrados: " + mensagem.getMensagem());
//            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
//        } else {
//            // obtem o objeto PERSISTIDO
//            Optional<Fabricante> fabricaSolicitada = fabricaRepository.findById(fabricaID);
//            fabricaInformada = fabricaSolicitada.get();
//            // remove a fabrica dos usuarios:
//            if( !fabricaInformada.getModelos().isEmpty() ) {
//                for ( Modelo book : fabricaInformada.getModelos() ) {
//                    fabricaInformada.removeModelo(book);
//                }
//            }
//            fabricaRepository.delete(fabricaInformada);
//            mensagem.setMensagem("Fabricante DELETADO no Sistema!");
//            logger.info("Requisicao executada: " + mensagem.getMensagem());
//        }
//        return new ResponseEntity<>(mensagem, HttpStatus.OK);
//    }
    
}
/*                    End of Class                                            */