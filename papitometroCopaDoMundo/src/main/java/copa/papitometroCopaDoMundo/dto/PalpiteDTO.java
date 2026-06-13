package copa.papitometroCopaDoMundo.dto;

import copa.papitometroCopaDoMundo.entitites.Palpite;

public class PalpiteDTO {

    private Long id;

    private Integer golsCasa;
    private Integer golsFora;
    private Integer pontos;

    private Long usuarioId;
    private String nomeUsuario;

    private Long jogoId;
    private String timeCasa;
    private String timeFora;

    public PalpiteDTO() {
    }
    
    public PalpiteDTO(Palpite entity) {
        id = entity.getId();
        golsCasa = entity.getGolsCasa();
        golsFora = entity.getGolsFora();
        pontos = entity.getPontos();

        usuarioId = entity.getUsuario().getId();
        nomeUsuario = entity.getUsuario().getNome();

        jogoId = entity.getJogo().getId();
        timeCasa = entity.getJogo().getTimeCasa();
        timeFora = entity.getJogo().getTimeFora();
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public Long getJogoId() {
        return jogoId;
    }

    public String getTimeCasa() {
        return timeCasa;
    }

    public String getTimeFora() {
        return timeFora;
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

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setJogoId(Long jogoId) {
        this.jogoId = jogoId;
    }

    public void setTimeCasa(String timeCasa) {
        this.timeCasa = timeCasa;
    }

    public void setTimeFora(String timeFora) {
        this.timeFora = timeFora;
    }
}