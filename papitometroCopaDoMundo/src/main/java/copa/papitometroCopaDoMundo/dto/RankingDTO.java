package copa.papitometroCopaDoMundo.dto;

public class RankingDTO {

    private Long usuarioId;
    private String nome;
    private Long pontos;
    private Long placaresExatos;
    private Long acertos;
    private Long palpites;

    public RankingDTO(Long usuarioId, String nome, Long pontos) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.pontos = pontos;
        this.placaresExatos = 0L;
        this.acertos = 0L;
        this.palpites = 0L;
    }

    public RankingDTO(Long usuarioId, String nome, Long pontos, Long placaresExatos, Long acertos, Long palpites) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.pontos = pontos;
        this.placaresExatos = placaresExatos;
        this.acertos = acertos;
        this.palpites = palpites;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public Long getPontos() {
        return pontos;
    }

    public Long getPlacaresExatos() {
        return placaresExatos;
    }

    public Long getAcertos() {
        return acertos;
    }

    public Long getPalpites() {
        return palpites;
    }
}
