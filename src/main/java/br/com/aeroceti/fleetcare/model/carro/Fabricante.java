/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.carro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 *  Objeto base Fabricante
 *
 * Esta classe representa um Fabricante de Carros.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Fabricante")
@Schema(name = "11. Fabricante") 
public class Fabricante implements Serializable {

    private static final long serialVersionUID = 3L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "entidadeID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long entidadeID;
    
    @NotNull
    @NotBlank
    @Column(name = "fabricanteName",        length = 50)
    @Size(min=5, message = "Nome precisa ter no mínimo 5 caracteres")
    @Schema(example = "Administrador", description = "Nome do Nivel de Acesso para os úsuarios")    
    private String fabricanteName;
    
    @Version
    @Column(name = "versao")
    private Long versao;

    @Schema(hidden = true)
    @OneToMany(mappedBy = "fabricante")
    private Set<Modelo> modelos = new HashSet<>();
   
    
    public Fabricante() {
    }

    public Fabricante(Long rID) {
        this.entidadeID = rID;
    }

    public Fabricante(Long id, String name) {
        this.entidadeID = id;
        this.fabricanteName = name;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the rolesID
     */
    public Long getEntidadeID() {
        return entidadeID;
    }

    public void setEntidadeID(Long entidadeID) {
        this.entidadeID = entidadeID;
    }

    public String getFabricanteName() {
        return fabricanteName;
    }

    public void setFabricanteName(String fabricanteName) {
        this.fabricanteName = fabricanteName;
    }

    /**
     * @return the versao
     */
    public Long getVersao() {
        return versao;
    }

    /**
     * @param versao the versao to set
     */
    public void setVersao(Long versao) {
        this.versao = versao;
    }

    @Override
    public boolean equals(Object obj) {
         // 1. Verificação de referência (se é o mesmo objeto na memória)
        if (this == obj) {
            return true;
        }
        // 2. Verificação de nulidade do objeto 'obj' e compatibilidade de classe
        if (!(obj instanceof Fabricante)) {
            return false;
        }
        // 3. Comparação dos atributos usando Objects.equals() para segurança contra NullPointerException
        Fabricante other = (Fabricante) obj;
        return Objects.equals(this.getEntidadeID(), other.getEntidadeID());
    }

    @Override
    public String toString() {
        return getFabricanteName() + "[ id=" + getEntidadeID() + "] ";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntidadeID());
    }
 

}
/*                    End of Class                                            */