package copa.papitometroCopaDoMundo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import copa.papitometroCopaDoMundo.dto.LoginDTO;
import copa.papitometroCopaDoMundo.dto.LoginResponseDTO;
import copa.papitometroCopaDoMundo.dto.UsuarioDTO;
import copa.papitometroCopaDoMundo.entitites.Usuario;
import copa.papitometroCopaDoMundo.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public LoginResponseDTO insert(UsuarioDTO dto) {

        Usuario entity = new Usuario();

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));

        entity = repository.save(entity);

        String token = jwtService.gerarToken(entity);

        return new LoginResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                token
        );
    }

    public LoginResponseDTO login(LoginDTO dto) {

        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        boolean senhaValida = passwordEncoder.matches(dto.getSenha(), usuario.getSenha());

        if (!senhaValida) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                token
        );
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());

        entity = repository.save(entity);

        return new UsuarioDTO(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}