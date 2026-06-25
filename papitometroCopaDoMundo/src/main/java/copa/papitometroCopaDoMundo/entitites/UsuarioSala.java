package copa.papitometroCopaDoMundo.entitites;

import jakarta.persistence.*;

@Entity
@Table(
    name = "tb_usuario_sala",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "sala_id"})
    }
)
public class UsuarioSala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;

    public UsuarioSala() {
    }

    public UsuarioSala(Long id, Usuario usuario, Sala sala) {
        this.id = id;
        this.usuario = usuario;
        this.sala = sala;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
}