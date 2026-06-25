package copa.papitometroCopaDoMundo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import copa.papitometroCopaDoMundo.dto.PalpiteDTO;
import copa.papitometroCopaDoMundo.dto.PalpiteJogoDTO;
import copa.papitometroCopaDoMundo.dto.RankingDTO;
import copa.papitometroCopaDoMundo.entitites.Jogo;
import copa.papitometroCopaDoMundo.entitites.Palpite;
import copa.papitometroCopaDoMundo.entitites.Sala;
import copa.papitometroCopaDoMundo.entitites.Usuario;
import copa.papitometroCopaDoMundo.repositories.JogoRepository;
import copa.papitometroCopaDoMundo.repositories.PalpiteRepository;
import copa.papitometroCopaDoMundo.repositories.SalaRepository;
import copa.papitometroCopaDoMundo.repositories.UsuarioRepository;

@Service
public class PalpiteService {

    @Autowired
    private PalpiteRepository palpiteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private SalaRepository salaRepository;

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
        validarDadosObrigatorios(dto);

        if (palpiteRepository.existsByUsuarioIdAndJogoIdAndSalaId(
                dto.getUsuarioId(), dto.getJogoId(), dto.getSalaId())) {
            throw new RuntimeException("Esse usuário já fez um palpite para esse jogo nessa sala");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Jogo jogo = jogoRepository.findById(dto.getJogoId())
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        Sala sala = salaRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        validarSePodePalpitar(jogo);

        Palpite entity = new Palpite();

        entity.setUsuario(usuario);
        entity.setJogo(jogo);
        entity.setSala(sala);
        entity.setGolsCasa(dto.getGolsCasa());
        entity.setGolsFora(dto.getGolsFora());
        entity.setPontos(0);

        entity = palpiteRepository.save(entity);

        return new PalpiteDTO(entity);
    }

    public PalpiteDTO update(Long id, PalpiteDTO dto) {
        Palpite entity = palpiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado"));

        if (dto.getGolsCasa() == null || dto.getGolsFora() == null) {
            throw new RuntimeException("Placar do palpite não informado");
        }

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

    private void validarDadosObrigatorios(PalpiteDTO dto) {
        if (dto.getUsuarioId() == null) {
            throw new RuntimeException("Usuário não informado");
        }

        if (dto.getJogoId() == null) {
            throw new RuntimeException("Jogo não informado");
        }

        if (dto.getSalaId() == null) {
            throw new RuntimeException("Sala não informada");
        }

        if (dto.getGolsCasa() == null || dto.getGolsFora() == null) {
            throw new RuntimeException("Placar do palpite não informado");
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

    public List<PalpiteDTO> findByUsuarioAndSala(Long usuarioId, Long salaId) {
        return palpiteRepository.findByUsuarioIdAndSalaId(usuarioId, salaId)
                .stream()
                .map(PalpiteDTO::new)
                .toList();
    }

    public List<RankingDTO> buscarRanking() {
        return palpiteRepository.buscarRanking();
    }

    public List<RankingDTO> buscarRankingPorSala(Long salaId) {
        return palpiteRepository.buscarRankingPorSala(salaId);
    }

    public List<PalpiteJogoDTO> buscarPalpitesPorJogo(Long jogoId) {
        return palpiteRepository.buscarPalpitesPorJogo(jogoId);
    }

    public List<PalpiteJogoDTO> buscarPalpitesPorJogoESala(Long jogoId, Long salaId) {
        return palpiteRepository.buscarPalpitesPorJogoESala(jogoId, salaId);
    }
}
