/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.carro;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.springframework.util.StringUtils;

/**
 *  Objeto base Modelo
 *
 * Esta classe representa um modelo de carro de um fabricante
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Modelo")
public class Modelo implements Serializable {

    private static final long serialVersionUID = 4L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "modeloID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modeloID;
    
    @NotNull
    @NotBlank
    @Size(min=5, message = "Nome precisa ter no mínimo 5 caracteres")
    @Column(name = "modeloName",        length = 50)
    private String modeloName;
    
    @Version
    @Column(name = "versao")
    private Long versao;

    @ManyToOne
    @JoinColumn(name = "fabricante")
    private Fabricante fabricante;    
    
    public Modelo() {
    }

    public Modelo(Long rID) {
        this.modeloID = rID;
    }

    public Modelo(Long modeloID, String modeloName) {
        this.modeloID = modeloID;
        this.modeloName = modeloName;
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
    public Long getModeloID() {
        return modeloID;
    }

    /**
     * @param rolesID the rolesID to set
     */
    public void setModeloID(Long rolesID) {
        this.modeloID = rolesID;
    }

    /**
     * @return the name
     */
    public String getModeloName() {
        return modeloName;
    }

    /**
     * @param name the name to set
     */
    public void setModeloName(String name) {
        name = StringUtils.capitalize(name);
        this.modeloName = name;
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

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }
 
//
//    public void addUsuario(Usuario user) {
//        this.usuarios.add(user);
//        user.getPermissoes().add(this);
//    }
//    
//    public void removeUsuario(Usuario user) {
//        this.usuarios.remove(user);
//        user.getPermissoes().remove(this);
//    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Modelo)) {
            return false;
        }
        Modelo other = (Modelo) obj;
        return !((this.getModeloID()== null && other.getModeloID() != null) || (this.getModeloID() != null && !this.modeloID.equals(other.modeloID) ) );
    }

    @Override
    public String toString() {
        return getModeloName() + "[ id=" + getModeloID() + "] ";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getModeloID() != null ? getModeloID().hashCode() : 0);
        return hash;
    }

}
/*                    End of Class                                            */