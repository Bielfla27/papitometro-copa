package copa.papitometroCopaDoMundo.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import copa.papitometroCopaDoMundo.dto.JogoDTO;
import copa.papitometroCopaDoMundo.entitites.Jogo;
import copa.papitometroCopaDoMundo.repositories.JogoRepository;


@Service
public class JogoService {

    @Autowired
    private JogoRepository repository;

    public List<JogoDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(JogoDTO::new)
                .toList();
    }

    public JogoDTO findById(Long id) {
        Jogo entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        return new JogoDTO(entity);
    }

    public JogoDTO insert(JogoDTO dto) {
        Jogo entity = new Jogo();

        entity.setTimeCasa(dto.getTimeCasa());
        entity.setTimeFora(dto.getTimeFora());
        entity.setDataHora(dto.getDataHora());
        entity.setGolsCasa(dto.getGolsCasa());
        entity.setGolsFora(dto.getGolsFora());
        entity.setEscudoTimeFora(dto.getEscudoTimeFora());
        entity.setEscudoTimeCasa(dto.getEscudoTimeCasa());
        entity = repository.save(entity);

        return new JogoDTO(entity);
    }

    public JogoDTO update(Long id, JogoDTO dto) {
        Jogo entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        entity.setTimeCasa(dto.getTimeCasa());
        entity.setTimeFora(dto.getTimeFora());
        entity.setDataHora(dto.getDataHora());
        entity.setGolsCasa(dto.getGolsCasa());
        entity.setGolsFora(dto.getGolsFora());

        entity = repository.save(entity);

        return new JogoDTO(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}