package copa.papitometroCopaDoMundo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import copa.papitometroCopaDoMundo.entitites.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}