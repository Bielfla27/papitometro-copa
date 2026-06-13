package copa.papitometroCopaDoMundo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import copa.papitometroCopaDoMundo.dto.JogoDTO;
import copa.papitometroCopaDoMundo.services.JogoService;

@RestController
@RequestMapping("/jogos")
@CrossOrigin(origins = "http://localhost:5173")
public class JogoController {

    @Autowired
    private JogoService service;
    
    @GetMapping
    public List<JogoDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public JogoDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public JogoDTO insert(@RequestBody JogoDTO dto) {
        return service.insert(dto);
    }

    @PutMapping("/{id}")
    public JogoDTO update(@PathVariable Long id, @RequestBody JogoDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}