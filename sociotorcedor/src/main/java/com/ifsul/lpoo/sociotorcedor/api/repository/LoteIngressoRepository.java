package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LoteIngressoRepository extends JpaRepository<LoteIngresso, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE LoteIngresso l SET l.ingressoDisponiveis = l.ingressoDisponiveis - :quantidade WHERE l.id = :id")
    void decrementarIngressosDisponiveis(@Param("id") Long id, @Param("quantidade") int quantidade);

    List<LoteIngresso> findAllByJogo_Id(Long jogoId);

    Boolean existsByJogoIdAndSetorId(Long jogoId, Long setorId);

    // Vendidos = ingressos com o status informado (ex.: PAGO) dos lotes do jogo.
    @Query("""
            select count(i)
            from Ingresso i
            where i.lote.jogo.id = :jogoId and i.status = :status
            """)
    Long contaIngressosVendidosPorJogo(@Param("jogoId") Long jogoId, @Param("status") String status);

    // Capacidade = soma dos ingressos que cada lote do jogo colocou à venda.
    @Query("select coalesce(sum(l.ingressosTotaisLote), 0) from LoteIngresso l where l.jogo.id = :jogoId")
    Long somaCapacidadePorJogo(@Param("jogoId") Long jogoId);

}
