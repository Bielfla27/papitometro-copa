package copa.papitometroCopaDoMundo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import copa.papitometroCopaDoMundo.dto.PalpiteDTO;
import copa.papitometroCopaDoMundo.services.PalpiteService;

@RestController
@RequestMapping("/palpites")
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
}