package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import jakarta.transaction.Transactional;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.Modifying;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.Query;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LoteIngressoRepository extends JpaRepository<LoteIngresso, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE LoteIngresso l SET l.ingressoDisponiveis = l.ingressoDisponiveis - :quantidade WHERE l.id = :id AND l.ingressoDisponiveis >= :quantidade")
    int decrementarIngressosDisponiveis(@Param("id") Long id, @Param("quantidade") int quantidade);

    List<LoteIngresso> findAllByJogo_Id(Long jogoId);

    Boolean existsByJogoIdAndSetorId(Long jogoId, Long setorId);

    @Query("""
            select count(i)
            from Ingresso i
            where i.lote.jogo.id = :jogoId and i.status = :status
            """)
    Long contaIngressosVendidosPorJogo(@Param("jogoId") Long jogoId, @Param("status") StatusIngresso status);

    @Query("select coalesce(sum(l.ingressosTotaisLote), 0) from LoteIngresso l where l.jogo.id = :jogoId")
    Long somaCapacidadePorJogo(@Param("jogoId") Long jogoId);

}
