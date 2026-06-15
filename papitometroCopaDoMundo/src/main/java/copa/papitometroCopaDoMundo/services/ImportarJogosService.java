package copa.papitometroCopaDoMundo.services;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import copa.papitometroCopaDoMundo.entitites.Jogo;
import copa.papitometroCopaDoMundo.repositories.JogoRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ImportarJogosService  {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private PalpiteService palpiteService;

    @Value("${football.api.token}")
    private String token;

    @Value("${football.api.url}")
    private String url;
    
    @PostConstruct
    public void iniciar() {
        importarJogos();
    }

    @Scheduled(fixedRate = 120000)
    public void atualizarJogosAutomaticamente() {
        System.out.println("Atualizando jogos automaticamente...");
        importarJogos();
      
    }

    public void importarJogos() {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Auth-Token", token);
            headers.set("User-Agent", "Mozilla/5.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );

            JsonNode body = response.getBody();

            if (body == null || body.get("matches") == null) {
                System.out.println("Nenhum jogo encontrado na API.");
                return;
            }

            JsonNode matches = body.get("matches");

            for (JsonNode match : matches) {

                Long apiId = match.get("id").asLong();

                String timeCasa = match.get("homeTeam").get("name").isNull()
                        ? "Não definido"
                        : match.get("homeTeam").get("name").asText();

                String timeFora = match.get("awayTeam").get("name").isNull()
                        ? "Não definido"
                        : match.get("awayTeam").get("name").asText();

                String escudoTimeCasa = match.get("homeTeam").get("crest").isNull()
                        ? null
                        : match.get("homeTeam").get("crest").asText();

                String escudoTimeFora = match.get("awayTeam").get("crest").isNull()
                        ? null
                        : match.get("awayTeam").get("crest").asText();

                String utcDate = match.get("utcDate").asText();

                LocalDateTime dataHora = OffsetDateTime
                        .parse(utcDate)
                        .atZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                        .toLocalDateTime();

                Integer golsCasa = null;
                Integer golsFora = null;

                String status = match.get("status").asText();

                JsonNode fullTime = match.get("score").get("fullTime");

                if (fullTime.get("home") != null && !fullTime.get("home").isNull()) {
                    golsCasa = fullTime.get("home").asInt();
                }

                if (fullTime.get("away") != null && !fullTime.get("away").isNull()) {
                    golsFora = fullTime.get("away").asInt();
                }

                Optional<Jogo> jogoOptional = jogoRepository.findByApiId(apiId);

                Jogo jogo;

                if (jogoOptional.isPresent()) {
                    jogo = jogoOptional.get();
                } else {
                    jogo = new Jogo();
                    jogo.setApiId(apiId);
                }

                jogo.setTimeCasa(timeCasa);
                jogo.setTimeFora(timeFora);
                jogo.setDataHora(dataHora);
                jogo.setGolsCasa(golsCasa);
                jogo.setGolsFora(golsFora);
                jogo.setEscudoTimeCasa(escudoTimeCasa);
                jogo.setEscudoTimeFora(escudoTimeFora);
                jogo.setStatus(status);

                jogo = jogoRepository.save(jogo);

             
                palpiteService.recalcularPontosDoJogo(jogo);
            }

            System.out.println("Jogos importados/atualizados com sucesso!");

        } catch (Exception e) {
            System.out.println("Não foi possível importar os jogos agora.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
