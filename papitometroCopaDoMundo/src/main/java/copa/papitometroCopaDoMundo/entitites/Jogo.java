package copa.papitometroCopaDoMundo.entitites;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String timeCasa;

    @Column(nullable = false)
    private String timeFora;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    private Integer golsCasa;

    private Integer golsFora;

    @OneToMany(mappedBy = "jogo")
    private List<Palpite> palpites = new ArrayList<>();

    @Column(unique = true)
    private Long apiId;
    
    private String escudoTimeCasa;
    private String escudoTimeFora;
    
   

	public Jogo() {
    }

    public Jogo(Long id, String timeCasa, String timeFora, LocalDateTime dataHora, Integer golsCasa, Integer golsFora) {
        this.id = id;
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.dataHora = dataHora;
        this.golsCasa = golsCasa;
        this.golsFora = golsFora;
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

    public List<Palpite> getPalpites() {
        return palpites;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimeCasa(String timeCasa) {
        this.timeCasa = timeCasa;
    }

    public Long getApiId() {
		return apiId;
	}

	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}

	public void setPalpites(List<Palpite> palpites) {
		this.palpites = palpites;
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