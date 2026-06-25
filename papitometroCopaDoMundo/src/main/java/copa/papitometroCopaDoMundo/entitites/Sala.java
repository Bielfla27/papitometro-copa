package copa.papitometroCopaDoMundo.entitites;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "dono_id")
    private Usuario dono;

    public Sala() {
    }

    public Sala(Long id, String nome, String codigo, Usuario dono) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.dono = dono;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }
    
}