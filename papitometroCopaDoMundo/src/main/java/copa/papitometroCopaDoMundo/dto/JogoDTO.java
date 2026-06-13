package copa.papitometroCopaDoMundo.dto;

import java.time.LocalDateTime;

import copa.papitometroCopaDoMundo.entitites.Jogo;

public class JogoDTO {

    private Long id;
    private String timeCasa;
    private String timeFora;
    private LocalDateTime dataHora;
    private Integer golsCasa;
    private Integer golsFora;
    private String escudoTimeCasa;
    private String escudoTimeFora;

   

	public JogoDTO() {
    }

    public JogoDTO(Long id, String timeCasa, String timeFora,
                   LocalDateTime dataHora,
                   Integer golsCasa,
                   Integer golsFora, String escudoTimeCasa, String escudoTimeFora) {

        this.id = id;
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.dataHora = dataHora;
        this.golsCasa = golsCasa;
        this.golsFora = golsFora;
        this.escudoTimeCasa = escudoTimeCasa;
        this.escudoTimeFora = escudoTimeFora;
    }
    
    public JogoDTO(Jogo entity) {
        this.id = entity.getId();
        this.timeCasa = entity.getTimeCasa();
        this.timeFora = entity.getTimeFora();
        this.dataHora = entity.getDataHora();
        this.golsCasa = entity.getGolsCasa();
        this.golsFora = entity.getGolsFora();
        this.escudoTimeCasa = entity.getEscudoTimeCasa();
        this.escudoTimeFora = entity.getEscudoTimeFora();
    }

    public Long getId() {
        return id;
    }

    public String getTimeCasa() {
        return timeCasa;
    }

    public String getTimeFora() {
        return timeFora;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Integer getGolsCasa() {
        return golsCasa;
    }

    public Integer getGolsFora() {
        return golsFora;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimeCasa(String timeCasa) {
        this.timeCasa = timeCasa;
    }

    public void setTimeFora(String timeFora) {
        this.timeFora = timeFora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setGolsCasa(Integer golsCasa) {
        this.golsCasa = golsCasa;
    }

    public void setGolsFora(Integer golsFora) {
        this.golsFora = golsFora;
    }
    
    public String getEscudoTimeCasa() {
		return escudoTimeCasa;
	}

	public void setEscudoTimeCasa(String escudoTimeCasa) {
		this.escudoTimeCasa = escudoTimeCasa;
	}

	public String getEscudoTimeFora() {
		return escudoTimeFora;
	}

	public void setEscudoTimeFora(String escudoTimeFora) {
		this.escudoTimeFora = escudoTimeFora;
	}
}