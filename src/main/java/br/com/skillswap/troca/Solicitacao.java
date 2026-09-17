package br.com.skillswap.troca;

import br.com.skillswap.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "de_usuario_id", nullable = false)
    private Usuario deUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "para_usuario_id", nullable = false)
    private Usuario paraUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    public Long getId() {
        return id;
    }

    public Usuario getDeUsuario() {
        return deUsuario;
    }

    public void setDeUsuario(Usuario deUsuario) {
        this.deUsuario = deUsuario;
    }

    public Usuario getParaUsuario() {
        return paraUsuario;
    }

    public void setParaUsuario(Usuario paraUsuario) {
        this.paraUsuario = paraUsuario;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
}
