package copa.papitometroCopaDoMundo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import copa.papitometroCopaDoMundo.dto.SalaDTO;
import copa.papitometroCopaDoMundo.services.SalaService;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @PostMapping
    public ResponseEntity<SalaDTO> criarSala(@RequestBody Map<String, Object> body) {

        String nome = body.get("nome").toString();
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());

        SalaDTO sala = salaService.criarSala(nome, usuarioId);

        return ResponseEntity.ok(sala);
    }

    @PostMapping("/entrar")
    public ResponseEntity<SalaDTO> entrarNaSala(@RequestBody Map<String, Object> body) {

        String codigo = body.get("codigo").toString();
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());

        SalaDTO sala = salaService.entrarNaSala(codigo, usuarioId);

        return ResponseEntity.ok(sala);
    }

    @GetMapping("/minhas/{usuarioId}")
    public ResponseEntity<List<SalaDTO>> minhasSalas(@PathVariable Long usuarioId) {

        List<SalaDTO> salas = salaService.minhasSalas(usuarioId);

        return ResponseEntity.ok(salas);
    }
}