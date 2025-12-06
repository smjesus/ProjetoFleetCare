/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.user;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 *  Objeto base Verificador (validacao de contas).
 *
 * Esta classe representa um UUID para verificar a conta do usuario.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "UsuarioVerificador")
public class UsuarioVerificador {
    private static final long serialVersionUID = 5L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "verificadorID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificadorID;
    
    private UUID codigoUUID;
    
    private Instant validade;
    
    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "usuario", referencedColumnName = "entidadeID", unique = true)
    private Usuario usuario;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the verificadorID
     */
    public Long getVerificadorID() {
        return verificadorID;
    }

    /**
     * @param verificadorID the verificadorID to set
     */
    public void setVerificadorID(Long verificadorID) {
        this.verificadorID = verificadorID;
    }

    /**
     * @return the codigoUUID
     */
    public UUID getCodigoUUID() {
        return codigoUUID;
    }

    /**
     * @param codigoUUID the codigoUUID to set
     */
    public void setCodigoUUID(UUID codigoUUID) {
        this.codigoUUID = codigoUUID;
    }

    /**
     * @return the validade
     */
    public Instant getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Instant validade) {
        this.validade = validade;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return this.getCodigoUUID()  + "[ID=" + this.getVerificadorID() + "]";
    }

    @Override
    public boolean equals(Object obj) {
         // 1. Verificação de referência (se é o mesmo objeto na memória)
        if (this == obj) {
            return true;
        }
        // 2. Verificação de nulidade do objeto 'obj' e compatibilidade de classe
        if (!(obj instanceof UsuarioVerificador)) {
            return false;
        }
        // 3. Comparação dos atributos usando Objects.equals() para segurança contra NullPointerException
        UsuarioVerificador other = (UsuarioVerificador) obj;
        return Objects.equals( this.getCodigoUUID() , other.getCodigoUUID() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVerificadorID());
    }
    
}
/*                    End of Class                                            */