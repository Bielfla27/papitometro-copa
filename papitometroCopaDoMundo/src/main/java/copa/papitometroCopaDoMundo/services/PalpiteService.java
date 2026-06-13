package copa.papitometroCopaDoMundo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import copa.papitometroCopaDoMundo.dto.PalpiteDTO;
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

        entity.setGolsCasa(dto.getGolsCasa());
        entity.setGolsFora(dto.getGolsFora());

        entity = palpiteRepository.save(entity);

        return new PalpiteDTO(entity);
    }

    public void delete(Long id) {
        palpiteRepository.deleteById(id);
    }
}