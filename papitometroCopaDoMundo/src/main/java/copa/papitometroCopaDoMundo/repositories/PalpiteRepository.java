package copa.papitometroCopaDoMundo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import copa.papitometroCopaDoMundo.dto.PalpiteJogoDTO;
import copa.papitometroCopaDoMundo.dto.RankingDTO;
import copa.papitometroCopaDoMundo.entitites.Jogo;
import copa.papitometroCopaDoMundo.entitites.Palpite;
import copa.papitometroCopaDoMundo.entitites.Sala;
import copa.papitometroCopaDoMundo.entitites.Usuario;


public interface PalpiteRepository extends JpaRepository<Palpite, Long> {
 
	Optional<Palpite> findByUsuarioAndJogoAndSala( Usuario usuario, Jogo jogo,Sala sala);
	
	 List<Palpite> findByJogoId(Long jogoId);
	 List<Palpite> findByUsuarioId(Long usuarioId);
	 
	 boolean existsByUsuarioIdAndJogoIdAndSalaId(Long usuarioId, Long jogoId, Long salaId);

	 List<Palpite> findByUsuarioIdAndSalaId(Long usuarioId, Long salaId);
	 
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
	 
	 
	 @Query("""
			    SELECT new copa.papitometroCopaDoMundo.dto.PalpiteJogoDTO(
			        p.usuario.id,
			        p.usuario.nome,
			        p.golsCasa,
			        p.golsFora,
			        p.pontos
			    )
			    FROM Palpite p
			    WHERE p.jogo.id = :jogoId
			    ORDER BY p.usuario.nome
			""")
			List<PalpiteJogoDTO> buscarPalpitesPorJogo(Long jogoId);
	 
	  List<Palpite> findBySala(Sala sala);
	  
	  
	  @Query("""
			    SELECT new copa.papitometroCopaDoMundo.dto.RankingDTO(
			        p.usuario.id,
			        p.usuario.nome,
			        SUM(p.pontos)
			    )
			    FROM Palpite p
			    WHERE p.sala.id = :salaId
			    GROUP BY p.usuario.id, p.usuario.nome
			    ORDER BY SUM(p.pontos) DESC
			""")
			List<RankingDTO> buscarRankingPorSala(@Param("salaId") Long salaId);
	  
	  
	  @Query("""
			    SELECT new copa.papitometroCopaDoMundo.dto.PalpiteJogoDTO(
			        p.usuario.id,
			        p.usuario.nome,
			        p.golsCasa,
			        p.golsFora,
			        p.pontos
			    )
			    FROM Palpite p
			    WHERE p.jogo.id = :jogoId
			      AND p.sala.id = :salaId
			    ORDER BY p.usuario.nome
			""")
			List<PalpiteJogoDTO> buscarPalpitesPorJogoESala(
			        @Param("jogoId") Long jogoId,
			        @Param("salaId") Long salaId
			);



}