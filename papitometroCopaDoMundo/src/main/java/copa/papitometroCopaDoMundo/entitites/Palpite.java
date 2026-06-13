package copa.papitometroCopaDoMundo.entitites;

import jakarta.persistence.*;

@Entity
@Table(
    name = "tb_palpite",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "jogo_id"})
    }
)
public class Palpite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer golsCasa;

    @Column(nullable = false)
    private Integer golsFora;

    private Integer pontos;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    public Palpite() {
    }

    public Palpite(Long id, Integer golsCasa, Integer golsFora, Integer pontos, Usuario usuario, Jogo jogo) {
        this.id = id;
        this.golsCasa = golsCasa;
        this.golsFora = golsFora;
        this.pontos = pontos;
        this.usuario = usuario;
        this.jogo = jogo;
    }

    public Long getId() {
        return id;
    }

    public Integer getGolsCasa() {
        return golsCasa;
    }

    public Integer getGolsFora() {
        return golsFora;
    }

    public Integer getPontos() {
        return pontos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGolsCasa(Integer golsCasa) {
        this.golsCasa = golsCasa;
    }

    public void setGolsFora(Integer golsFora) {
        this.golsFora = golsFora;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }
}