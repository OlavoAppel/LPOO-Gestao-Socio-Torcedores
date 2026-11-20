package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    // coalesce: sum() devolve null quando não há linhas; assim devolvemos 0.
    @Query("select coalesce(sum(i.precoPago), 0) from Ingresso i where i.status = :status")
    BigDecimal somarReceitaPorStatus(@Param("status") String status);

    @Query("""
            select coalesce(sum(i.precoPago), 0) from Ingresso i
            where i.status = :status and i.dhCompra >= :inicio and i.dhCompra < :fim
            """)
    BigDecimal somarReceitaPorStatusNoPeriodo(@Param("status") String status,
                                              @Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim);

    long countByStatusAndDhCompraGreaterThanEqualAndDhCompraLessThan(String status,
                                                                      LocalDateTime inicio,
                                                                      LocalDateTime fim);

    @Query("""
            select coalesce(sum(i.precoPago), 0) from Ingresso i
            where i.lote.jogo.id = :jogoId and i.status = 'PAGO'
            """)
    BigDecimal contaReceitaPorJogo(@Param("jogoId") Long jogoId);

}


