/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package com.aeroceti.fleetcare.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 *  Objeto base Usuario.
 *
 * Esta classe representa um Usuario no sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Usuario", uniqueConstraints = {
       @UniqueConstraint(columnNames = {"CPF"})})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "usuarioID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID usuarioID;
    
    @Column(name = "CPF",            length = 11)
    private String cpf             = "";
    @Column(name = "nome",           length = 50)
    private String nome            = "";
    @Column(name = "email",          length = 45)
    private String email           = "";
    @Column(name = "whatsapp",       length = 20)
    private String whatsapp        = "";
    @Column(name = "dataNascimento", length = 10)
    private String dataNascimento  = "";
    @Column(name = "sexo",           length = 1)
    private String sexo            = "";
    @Column(name = "ativo")
    private boolean ativo          = true ;
    
    @Column(name = "password",       length = 128)
    private String password        = "";
    
    @Version
    @Column(name = "versao")
    private Long versao;

    public Usuario() {
    }

    public Usuario(UUID usuarioID) {
        this.usuarioID = usuarioID;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the usuarioID
     */
    public UUID getUsuarioID() {
        return usuarioID;
    }

    /**
     * @param usuarioID the usuarioID to set
     */
    public void setUsuarioID(UUID usuarioID) {
        this.usuarioID = usuarioID;
    }

    /**
     * Verifica se o objeto USUARIO está ativo no sistema
     * @return True ou False
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Modifica o status do USUARIO no sistema
     * @param ativo - (True ou False) conforme o caso
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Retorna o CPF do colaborador no formato SOMENTE Numeros.
     * @return cpf - String nao formatada contendo o CPF.
     */
    public String getCleanCPF() {
        return cpf ;
    }
    
    /**
     * Retorna o CPF do colaborador FORMATADO.
     * @return cpf - String contendo o CPF Formatado.
     */
    public String getCpf() {
       return cpf.replaceAll( "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4" ).trim();
    }

    /**
     * GRAVA o CPF do colaborador sem formatacao (somente numeros).
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf.replaceAll("\\D", "").trim();
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        try {
            String[] partes = nome.split(" ");
            StringBuilder sb = new StringBuilder();
            String [] codes = {"da", "de", "do", "di", "dos", "das", "e", "d'"};
            for (int i = 0; i < partes.length; i++) {
                String parte = partes[i];
                if( !parte.trim().equals("") ) {
                    if( i == 0 ) {
                        parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                    } else  {
                        if(Arrays.asList(codes).contains( parte.toLowerCase().trim()) ) {
                            parte = parte.toLowerCase();
                        }  else  {
                            parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                        }
                    }
                    sb.append(" ").append(parte.trim());
                }
            }
            this.nome = sb.toString().trim();
        } catch(Exception nameError) {
            this.nome = "" ;
        }
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public void setWhatsapp(String whatsapp) {
        String novoValor;
        try {
            novoValor = whatsapp.trim();
            //  Limpa o telefone das formatacoes:
            novoValor = novoValor.replaceAll("\\D", "");
            // corrige o problema da formatacao do Bootstrap:
            if( novoValor.trim().length() == 1 ) {
                    novoValor = "";
            }
        } catch(NullPointerException nulo) {
            novoValor = "";  
        }
        this.whatsapp = novoValor;
    }

    public String getWhatsapp() {
        if ( this.whatsapp.length() == 10 ) {
            return this.whatsapp.replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1)$2-$3");
        } else {
           return this.whatsapp.replaceFirst("(\\d{2})(\\d{1})(\\d{4})(\\d+)", "($1)$2.$3-$4");
        }
    }

    /**
     * @return the dataNascimento
     */
    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(String dataNascimento) {
        dataNascimento = dataNascimento.replaceAll("\\D", "");
        this.dataNascimento = dataNascimento.replaceAll( "([0-9]{2})([0-9]{2})([0-9]{4})", "$1/$2/$3" ).trim();
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
    public int hashCode() {
        int hash = 0;
        hash += (getUsuarioID() != null ? getUsuarioID().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.getUsuarioID() == null && other.getUsuarioID() != null) || (this.getUsuarioID() != null && !this.usuarioID.equals(other.usuarioID)));
    }

    @Override
    public String toString() {
        return getNome() + "[ id=" + getUsuarioID() + " - e-mail=" + getEmail() + " ]";
    }
    
}
