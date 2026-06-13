package copa.papitometroCopaDoMundo.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import copa.papitometroCopaDoMundo.dto.UsuarioDTO;
import copa.papitometroCopaDoMundo.entitites.Usuario;
import copa.papitometroCopaDoMundo.repositories.UsuarioRepository;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<UsuarioDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .toList();
    }

    public UsuarioDTO findById(Long id) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UsuarioDTO(entity);
    }

    public UsuarioDTO insert(UsuarioDTO dto) {
        Usuario entity = new Usuario();

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());

        entity = repository.save(entity);

        return new UsuarioDTO(entity);
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            entity.setSenha(dto.getSenha());
        }

        entity = repository.save(entity);

        return new UsuarioDTO(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    public UsuarioDTO login(UsuarioDTO dto) {
        Usuario usuario = repository.findByEmailAndSenha(dto.getEmail(), dto.getSenha())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        return new UsuarioDTO(usuario);
    }
}