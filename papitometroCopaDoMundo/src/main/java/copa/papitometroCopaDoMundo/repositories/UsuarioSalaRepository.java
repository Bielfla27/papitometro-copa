package copa.papitometroCopaDoMundo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import copa.papitometroCopaDoMundo.entitites.Sala;
import copa.papitometroCopaDoMundo.entitites.Usuario;
import copa.papitometroCopaDoMundo.entitites.UsuarioSala;

public interface UsuarioSalaRepository extends JpaRepository<UsuarioSala, Long> {

    Optional<UsuarioSala> findByUsuarioAndSala(Usuario usuario, Sala sala);

    List<UsuarioSala> findByUsuario(Usuario usuario);
}