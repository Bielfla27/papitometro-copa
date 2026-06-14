package copa.papitometroCopaDoMundo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import copa.papitometroCopaDoMundo.entitites.Jogo;


public interface JogoRepository extends JpaRepository<Jogo, Long> {

	boolean existsByApiId(Long apiId);
	Optional<Jogo> findByApiId(Long apiId);
}