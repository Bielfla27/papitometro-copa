package copa.papitometroCopaDoMundo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import copa.papitometroCopaDoMundo.dto.LoginDTO;
import copa.papitometroCopaDoMundo.dto.LoginResponseDTO;
import copa.papitometroCopaDoMundo.dto.UsuarioDTO;
import copa.papitometroCopaDoMundo.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<UsuarioDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public LoginResponseDTO insert(@RequestBody UsuarioDTO dto) {
        return service.insert(dto);
    }
    @PutMapping("/{id}")
    public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO dto) {
        return service.login(dto);
    }
}
