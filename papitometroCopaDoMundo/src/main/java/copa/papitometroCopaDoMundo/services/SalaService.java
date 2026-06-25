package copa.papitometroCopaDoMundo.services;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import copa.papitometroCopaDoMundo.dto.SalaDTO;
import copa.papitometroCopaDoMundo.entitites.Sala;
import copa.papitometroCopaDoMundo.entitites.Usuario;
import copa.papitometroCopaDoMundo.entitites.UsuarioSala;
import copa.papitometroCopaDoMundo.repositories.SalaRepository;
import copa.papitometroCopaDoMundo.repositories.UsuarioRepository;
import copa.papitometroCopaDoMundo.repositories.UsuarioSalaRepository;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioSalaRepository usuarioSalaRepository;

    public SalaService(
            SalaRepository salaRepository,
            UsuarioRepository usuarioRepository,
            UsuarioSalaRepository usuarioSalaRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioSalaRepository = usuarioSalaRepository;
    }

    @Transactional
    public SalaDTO criarSala(String nome, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Sala sala = new Sala();
        sala.setNome(nome);
        sala.setDono(usuario);
        sala.setCodigo(gerarCodigoUnico());

        sala = salaRepository.save(sala);

        UsuarioSala usuarioSala = new UsuarioSala();
        usuarioSala.setUsuario(usuario);
        usuarioSala.setSala(sala);

        usuarioSalaRepository.save(usuarioSala);

        return new SalaDTO(sala);
    }

    @Transactional
    public SalaDTO entrarNaSala(String codigo, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Sala sala = salaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        boolean jaEstaNaSala = usuarioSalaRepository
                .findByUsuarioAndSala(usuario, sala)
                .isPresent();

        if (!jaEstaNaSala) {
            UsuarioSala usuarioSala = new UsuarioSala();
            usuarioSala.setUsuario(usuario);
            usuarioSala.setSala(sala);
            usuarioSalaRepository.save(usuarioSala);
        }

        return new SalaDTO(sala);
    }

    @Transactional(readOnly = true)
    public List<SalaDTO> minhasSalas(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return usuarioSalaRepository.findByUsuario(usuario)
                .stream()
                .map(us -> new SalaDTO(us.getSala()))
                .toList();
    }

    private String gerarCodigoUnico() {
        String codigo;

        do {
            codigo = gerarCodigo();
        } while (salaRepository.findByCodigo(codigo).isPresent());

        return codigo;
    }

    private String gerarCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder sb = new StringBuilder("SALA-");

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return sb.toString();
    }
}