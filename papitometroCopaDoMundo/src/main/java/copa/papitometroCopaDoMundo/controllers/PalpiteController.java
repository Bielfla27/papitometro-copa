package copa.papitometroCopaDoMundo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import copa.papitometroCopaDoMundo.dto.PalpiteDTO;
import copa.papitometroCopaDoMundo.dto.PalpiteJogoDTO;
import copa.papitometroCopaDoMundo.dto.RankingDTO;
import copa.papitometroCopaDoMundo.services.PalpiteService;

@RestController
@RequestMapping("/palpites")
@CrossOrigin(origins = "http://localhost:5173")
public class PalpiteController {

    @Autowired
    private PalpiteService service;

    @GetMapping
    public List<PalpiteDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PalpiteDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PalpiteDTO insert(@RequestBody PalpiteDTO dto) {
        return service.insert(dto);
    }

    @PutMapping("/{id}")
    public PalpiteDTO update(@PathVariable Long id, @RequestBody PalpiteDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<PalpiteDTO> findByUsuario(@PathVariable Long usuarioId) {
        return service.findByUsuario(usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}/sala/{salaId}")
    public List<PalpiteDTO> findByUsuarioAndSala(
            @PathVariable Long usuarioId,
            @PathVariable Long salaId) {

        return service.findByUsuarioAndSala(usuarioId, salaId);
    }

    @GetMapping("/ranking")
    public List<RankingDTO> buscarRanking() {
        return service.buscarRanking();
    }

    @GetMapping("/ranking/sala/{salaId}")
    public List<RankingDTO> buscarRankingPorSala(@PathVariable Long salaId) {
        return service.buscarRankingPorSala(salaId);
    }

    @GetMapping("/jogo/{jogoId}")
    public List<PalpiteJogoDTO> buscarPalpitesPorJogo(@PathVariable Long jogoId) {
        return service.buscarPalpitesPorJogo(jogoId);
    }

    @GetMapping("/jogo/{jogoId}/sala/{salaId}")
    public List<PalpiteJogoDTO> buscarPalpitesPorJogoESala(
            @PathVariable Long jogoId,
            @PathVariable Long salaId) {

        return service.buscarPalpitesPorJogoESala(jogoId, salaId);
    }
}