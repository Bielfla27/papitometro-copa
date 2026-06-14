package copa.papitometroCopaDoMundo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import copa.papitometroCopaDoMundo.dto.RankingDTO;
import copa.papitometroCopaDoMundo.entitites.Palpite;


public interface PalpiteRepository extends JpaRepository<Palpite, Long> {

	 boolean existsByUsuarioIdAndJogoId(Long usuarioId, Long jogoId);
	 List<Palpite> findByJogoId(Long jogoId);
	 List<Palpite> findByUsuarioId(Long usuarioId);
	 
	 @Query("""
		        SELECT new copa.papitometroCopaDoMundo.dto.RankingDTO(
		            p.usuario.id,
		            p.usuario.nome,
		            COALESCE(SUM(p.pontos), 0)
		        )
		        FROM Palpite p
		        GROUP BY p.usuario.id, p.usuario.nome
		        ORDER BY COALESCE(SUM(p.pontos), 0) DESC
		    """)
		    List<RankingDTO> buscarRanking();
}