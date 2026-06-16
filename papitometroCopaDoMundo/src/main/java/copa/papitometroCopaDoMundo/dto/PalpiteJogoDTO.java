package copa.papitometroCopaDoMundo.dto;

public class PalpiteJogoDTO {

    private Long usuarioId;
    private String usuarioNome;
    private Integer golsCasa;
    private Integer golsFora;
    private Integer pontos;

    public PalpiteJogoDTO(Long usuarioId, String usuarioNome, Integer golsCasa, Integer golsFora, Integer pontos) {
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.golsCasa = golsCasa;
        this.golsFora = golsFora;
        this.pontos = pontos;
    }

	public Long getUsuarioId() {
		return usuarioId;
	}

	public String getUsuarioNome() {
		return usuarioNome;
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

   
}