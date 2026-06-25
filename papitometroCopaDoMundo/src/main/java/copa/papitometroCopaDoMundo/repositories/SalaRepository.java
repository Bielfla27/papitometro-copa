package copa.papitometroCopaDoMundo.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import copa.papitometroCopaDoMundo.entitites.Sala;


public interface SalaRepository extends JpaRepository<Sala, Long> {

    Optional<Sala> findByCodigo(String codigo);
}