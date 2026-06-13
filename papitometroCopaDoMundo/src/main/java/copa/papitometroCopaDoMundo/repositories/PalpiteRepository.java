package copa.papitometroCopaDoMundo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import copa.papitometroCopaDoMundo.entitites.Palpite;


public interface PalpiteRepository extends JpaRepository<Palpite, Long> {

	 boolean existsByUsuarioIdAndJogoId(Long usuarioId, Long jogoId);
	 
}