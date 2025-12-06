/**
 * Projeto:  Fleet Care Amazon  -  Sistema de Controle de Locacao e Manutencao de Veiculos.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2023
 * Equipe:   Murilo, Victor
 */
package br.com.aeroceti.fleetcare.model.user;

import java.util.Set;
import java.util.Objects;
import java.util.HashSet;
import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import io.swagger.v3.oas.annotations.media.Schema;
import br.com.aeroceti.fleetcare.model.carro.Carro;
import br.com.aeroceti.fleetcare.components.StringTools;
import br.com.aeroceti.fleetcare.components.SenhasIguais;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.Pattern;

/**
 *  Objeto base Usuario.
 *
 * Esta classe representa um Usuario no sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@SenhasIguais
@Table(name = "Usuario", uniqueConstraints = {
@UniqueConstraint(columnNames = {"CPF"})})
@Schema(name = "02. Usuario") 
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "entidadeID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long entidadeID;
    
    @NotBlank
    @Size(min=11, max=11)
    @CPF(message = "CPF inválido!")
    @Column(name = "CPF",            length = 11)
    @Schema(example = "111.222.333-99", description = "CPF Válido da Usuário")
    private String cpf             = "";
    
    @NotNull
    @NotBlank
    @Size(min=5, max=50, message = "Nome precisa ter no mínimo 5 caracteres")
    @Column(name = "nome",           length = 50)
    private String nome            = "";

    @NotNull
    @NotBlank
    @Size(min=5, max=50, message = "Sobrenome precisa ter no mínimo 5 caracteres")
    @Column(name = "sobrenome",      length = 50)
    private String sobrenome       = "";

    @NotNull
    @NotBlank
    @Size(min=6, max=45)
    @Email(message = "Email inválido")
    @Column(name = "email",          length = 45)
    private String email           = "";

    @Basic
    @NotNull
    @NotBlank
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "dataNascimento",     length =  10)
    @Size(min=8, max=10, message = "Data de nascimento do Colaborador no formato dd/mm/AAAA")
    private String dataNascimento  = "";

    @Column(name = "whatsapp",           length =  20)
    @Size(min=0, max=20, message = "Telefone de contato com Whatsapp")
    private String whatsapp              = "";
    
    @Size(min=0, max=1)
    @Column(name = "sexo",               length =   1)
    private String sexo                  = "";

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "A senha deve conter letras maiúsculas, minúsculas, números e símbolos."
    )
    @Column(name = "password",           length = 128)
    private String password              = "";
    
    @Column(name = "ativo")
    private boolean ativo          = false ;
    
    @Version
    @Column(name = "versao")
    private Long versao;

    @Transient
    private String confirmarSenha;
    
    @ManyToOne
    @JsonManagedReference("nivelAcesso")
    @JoinColumn(name = "nivelAcesso", nullable = true)
    @Schema(description = "Nível de Acesso que um Colaborador possue no sistema", implementation = NivelAcesso.class)
    private NivelAcesso nivelAcesso;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Schema(hidden = true)
    private Set<Carro> carros = new HashSet<>();
    
    public Usuario() {
    }

    public Usuario(Long usuarioID) {
        this.entidadeID = usuarioID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the entidadeID
     */
    public Long getEntidadeID() {
        return entidadeID;
    }

    public void setEntidadeID(Long entidadeID) {
        this.entidadeID = entidadeID;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso permissao) {
        this.nivelAcesso = permissao;
    }

    // Grava o CPF sem formatacao:
    public void setCpf(String cpf) {
        this.cpf = cpf.replaceAll("\\D", "").trim();
    }
    
    // obtem o CPF FORMATADO 111.222.333-44
    public String getCpf() {
       return cpf.replaceAll( "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4" ).trim();
    }

    // obtem o CPF SEM FORMATACAO
    public String getCleanCPF() {
        return cpf ;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    // Obtem o Nome Completo do usuario
    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }
        
    // Formata o nome fornecido
    public void setNome(String nome) {
        this.nome = StringTools.formatName(nome) ;
    }

    // Formata o sobrenome fornecido
    public void setSobrenome(String sobrenome) {
        this.sobrenome = StringTools.formatName(sobrenome) ;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String novoValor;
        try {
                novoValor = email.toLowerCase().trim();
        } catch(NullPointerException nulo) {
                novoValor = "";  
        }
        this.email = novoValor ;
    }

    /*   Guarda o numero do TELEFONE sem formatacao (SOMENTE numeros).
     *  Este metodo elimina tambem o 0 (Zero) inicial, mantendo o formato: 
     *  XX988887777 (XX codigo DDD) ou XX33335555 (telefone fixo).
     */
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

    /*
     * Obtem o numero do Telefone do Colaborador Formatado no padrao
     * (99)9.8888-7777  ou  (99)4444-2222 (fixo).
     */    
    public String getWhatsapp() {
        if ( this.whatsapp.length() == 10 ) {
            return this.whatsapp.replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1)$2-$3");
        } else {
           return this.whatsapp.replaceFirst("(\\d{2})(\\d{1})(\\d{4})(\\d+)", "($1)$2.$3-$4");
        }
    }

    public String getDataNascimento() {
        return dataNascimento.replaceAll( "([0-9]{2})([0-9]{2})([0-9]{4})", "$1/$2/$3" ).trim();
    }

    // Grava a data no formato ddmmaaaa    
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento.replaceAll("\\D", "");
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Set<Carro> getCarros() {
        return carros;
    }

    public void setCarros(Set<Carro> carros) {
        this.carros = carros;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getEntidadeID());
    }

    @Override
    public String toString() {
        return getNome() + "[ id=" + getEntidadeID() + " - e-mail=" + getEmail() + " ]";
    }

    @Override
    public boolean equals(Object obj) {
         // 1. Verificação de referência (se é o mesmo objeto na memória)
        if (this == obj) {
            return true;
        }
        // 2. Verificação de nulidade do objeto 'obj' e compatibilidade de classe
        if (!(obj instanceof Usuario)) {
            return false;
        }
        // 3. Comparação dos atributos usando Objects.equals() para segurança contra NullPointerException
        Usuario other = (Usuario) obj;
        return Objects.equals(this.getEntidadeID(), other.getEntidadeID());
    }
    
}
/*                    End of Class                                            */