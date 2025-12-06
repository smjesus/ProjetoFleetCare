/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.carro;

import br.com.aeroceti.fleetcare.model.user.Usuario;
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
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *  Objeto base Carro
 *
 * Esta classe representa um Carro do usuário.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Carro")
public class Carro implements Serializable {

    private static final long serialVersionUID = 5L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "carroID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carroID;
    
    @NotNull
    @NotBlank
    @Size(min=5, message = "Número da Placa precisa ter 7 caracteres")
    @Column(name = "numeroDaPlaca",        length = 7)
    private String numeroDaPlaca;
    
    @NotNull
    @NotBlank
    @Size(min=4, message = "Ano do Veículo precisa ter 4 caracteres")
    @Column(name = "anoDoCarro",        length = 4)
    private String anoDoCarro;
    
    @Version
    @Column(name = "versao")
    private Long versao;

    @ManyToOne
    @JoinColumn(name = "modelo")
    private Modelo modelo;     
    
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    public Carro() {
    }

    public Carro(Long rID) {
        this.carroID = rID;
    }

    public Carro(Long id, String placa) {
        this.carroID = id;
        this.numeroDaPlaca = placa;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Long getCarroID() {
        return carroID;
    }

    /**
     * @param carroID the carroID to set
     */
    public void setCarroID(Long carroID) {
        this.carroID = carroID;
    }

    /**
     * @return the numeroDaPlaca
     */
    public String getNumeroDaPlaca() {
        return numeroDaPlaca;
    }

    /**
     * @param numeroDaPlaca the numeroDaPlaca to set
     */
    public void setNumeroDaPlaca(String numeroDaPlaca) {
        this.numeroDaPlaca = numeroDaPlaca;
    }

    public String getAnoDoCarro() {
        return anoDoCarro;
    }

    public void setAnoDoCarro(String anoDoCarro) {
        this.anoDoCarro = anoDoCarro;
    }
    
    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Carro)) {
            return false;
        }
        Carro other = (Carro) obj;
        return !((this.getCarroID() == null && other.getCarroID() != null) || 
                (this.getCarroID() != null && !this.carroID.equals(other.carroID) ) );
    }

    @Override
    public String toString() {
        return getNumeroDaPlaca() + "[ id=" + getCarroID() + "] ";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCarroID() != null ? getCarroID().hashCode() : 0);
        return hash;
    }
    
}
/*                    End of Class                                            */