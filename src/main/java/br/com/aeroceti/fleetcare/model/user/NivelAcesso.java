/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.user;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.persistence.OneToMany;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 *  Objeto base NivelAcesso (Niveis de usuarios).
 *
 * Esta classe representa um nivel de um usuario no sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "NivelAcesso")
@Schema(name = "01. NivelAcesso") 
public class NivelAcesso implements Serializable {

    private static final long serialVersionUID = 2L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "entidadeID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long entidadeID;
    
    @NotNull(message = "{roles.nome.notnull}")
    @NotBlank(message = "{roles.nome.notblank}")
    @Size(min=5, max=50, message = "{roles.nome.size.error}")
    @Schema(example = "Administrador", description = "Nome do Nivel de Acesso para os úsuarios")
    @Column(name = "nome",        length = 50)
    private String nome;
    
    @Version
    @Column(name = "versao")
    @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
    private Long versao;

    @JsonBackReference("nivelAcesso")
    @OneToMany(mappedBy = "nivelAcesso")
    @Schema(hidden = true)
    private Set<Usuario> usuarios = new HashSet<Usuario>();    
    
    public NivelAcesso() {
    }

    public NivelAcesso(Long rID) {
        this.entidadeID = rID;
    }

    public NivelAcesso(Long rulesID, String ruleName) {
        this.entidadeID = rulesID;
        this.nome = ruleName;
    }

    public NivelAcesso(Long rolesID, String roleName, Long versao) {
        this.entidadeID = rolesID;
        this.nome = roleName;
        this.versao = versao;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getEntidadeID() {
        return entidadeID;
    }

    public void setEntidadeID(Long rolesID) {
        this.entidadeID = rolesID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String roleName) {
        roleName = StringUtils.capitalize(roleName);
        this.nome = roleName;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }    

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntidadeID());
    }
 
    @Override
    public String toString() {
        return getNome() + "[ id=" + getEntidadeID() + "] ";
    }

    @Override
    public boolean equals(Object obj) {
         // 1. Verificação de referência (se é o mesmo objeto na memória)
        if (this == obj) {
            return true;
        }
        // 2. Verificação de nulidade do objeto 'obj' e compatibilidade de classe
        if (!(obj instanceof NivelAcesso)) {
            return false;
        }
        // 3. Comparação dos atributos usando Objects.equals() para segurança contra NullPointerException
        NivelAcesso other = (NivelAcesso) obj;
        return Objects.equals(this.getEntidadeID(), other.getEntidadeID());
    }

}
/*                    End of Class                                            */