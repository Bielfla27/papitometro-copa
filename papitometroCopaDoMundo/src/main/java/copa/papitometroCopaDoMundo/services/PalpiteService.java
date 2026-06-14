package copa.papitometroCopaDoMundo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import copa.papitometroCopaDoMundo.dto.PalpiteDTO;
import copa.papitometroCopaDoMundo.dto.RankingDTO;
import copa.papitometroCopaDoMundo.entitites.Jogo;
import copa.papitometroCopaDoMundo.entitites.Palpite;
import copa.papitometroCopaDoMundo.entitites.Usuario;
import copa.papitometroCopaDoMundo.repositories.JogoRepository;
import copa.papitometroCopaDoMundo.repositories.PalpiteRepository;
import copa.papitometroCopaDoMundo.repositories.UsuarioRepository;

@Service
public class PalpiteService {

    @Autowired
    private PalpiteRepository palpiteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JogoRepository jogoRepository;

    public List<PalpiteDTO> findAll() {
        return palpiteRepository.findAll()
                .stream()
                .map(PalpiteDTO::new)
                .toList();
    }

    public PalpiteDTO findById(Long id) {
        Palpite entity = palpiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado"));

        return new PalpiteDTO(entity);
    }

    public PalpiteDTO insert(PalpiteDTO dto) {

        if (palpiteRepository.existsByUsuarioIdAndJogoId(dto.getUsuarioId(), dto.getJogoId())) {
            throw new RuntimeException("Esse usuário já fez um palpite para esse jogo");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Jogo jogo = jogoRepository.findById(dto.getJogoId())
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        validarSePodePalpitar(jogo);

        Palpite entity = new Palpite();

        entity.setUsuario(usuario);
        entity.setJogo(jogo);
        entity.setGolsCasa(dto.getGolsCasa());
        entity.setGolsFora(dto.getGolsFora());
        entity.setPontos(0);

        entity = palpiteRepository.save(entity);

        return new PalpiteDTO(entity);
    }

    public PalpiteDTO update(Long id, PalpiteDTO dto) {
        Palpite entity = palpiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado"));

        validarSePodePalpitar(entity.getJogo());

        entity.setGolsCasa(dto.getGolsCasa());
        entity.setGolsFora(dto.getGolsFora());

        entity = palpiteRepository.save(entity);

        return new PalpiteDTO(entity);
    }

    public void delete(Long id) {
        palpiteRepository.deleteById(id);
    }

    public void recalcularPontosDoJogo(Jogo jogo) {

        if (jogo.getGolsCasa() == null || jogo.getGolsFora() == null) {
            return;
        }

        List<Palpite> palpitesDoJogo = palpiteRepository.findByJogoId(jogo.getId());

        for (Palpite palpite : palpitesDoJogo) {
            Integer pontos = calcularPontos(palpite, jogo);
            palpite.setPontos(pontos);
            palpiteRepository.save(palpite);
        }
    }

    private void validarSePodePalpitar(Jogo jogo) {
        String status = jogo.getStatus();

        if ("FINISHED".equalsIgnoreCase(status)
                || "LIVE".equalsIgnoreCase(status)
                || "IN_PLAY".equalsIgnoreCase(status)
                || "PAUSED".equalsIgnoreCase(status)) {
            throw new RuntimeException("Não é possível palpitar em jogo ao vivo ou finalizado");
        }
    }

    private Integer calcularPontos(Palpite palpite, Jogo jogo) {

        if (jogo.getGolsCasa() == null || jogo.getGolsFora() == null) {
            return 0;
        }

        boolean placarExato =
                palpite.getGolsCasa().equals(jogo.getGolsCasa())
                && palpite.getGolsFora().equals(jogo.getGolsFora());

        if (placarExato) {
            return 3;
        }

        int resultadoReal = Integer.compare(jogo.getGolsCasa(), jogo.getGolsFora());
        int resultadoPalpite = Integer.compare(palpite.getGolsCasa(), palpite.getGolsFora());

        if (resultadoReal == resultadoPalpite) {
            return 1;
        }

        return 0;
    }
    
    public List<PalpiteDTO> findByUsuario(Long usuarioId) {

        return palpiteRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(PalpiteDTO::new)
                .toList();
    }
    
    public List<RankingDTO> buscarRanking() {
        return palpiteRepository.buscarRanking();
    }
}