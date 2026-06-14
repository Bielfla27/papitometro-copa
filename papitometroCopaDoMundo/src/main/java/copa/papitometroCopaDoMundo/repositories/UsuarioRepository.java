package copa.papitometroCopaDoMundo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import copa.papitometroCopaDoMundo.entitites.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByEmail(String email);

}