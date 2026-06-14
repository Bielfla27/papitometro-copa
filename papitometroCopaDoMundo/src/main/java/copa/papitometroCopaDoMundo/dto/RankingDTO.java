package copa.papitometroCopaDoMundo.dto;

public class RankingDTO {

    private Long usuarioId;
    private String nome;
    private Long pontos;

    public RankingDTO(Long usuarioId, String nome, Long pontos) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.pontos = pontos;
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
}