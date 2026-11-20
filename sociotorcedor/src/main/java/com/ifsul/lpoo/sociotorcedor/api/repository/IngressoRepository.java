package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.Ingresso;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.Query;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    // coalesce: sum() devolve null quando não há linhas; assim devolvemos 0.
    @Query("select coalesce(sum(i.precoPago), 0) from Ingresso i where i.status = :status")
    BigDecimal somarReceitaPorStatus(@Param("status") StatusIngresso status);

    @Query("""
            select coalesce(sum(i.precoPago), 0) from Ingresso i
            where i.status = :status and i.dhCompra >= :inicio and i.dhCompra < :fim
            """)
    BigDecimal somarReceitaPorStatusNoPeriodo(@Param("status") StatusIngresso status,
                                              @Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim);

    long countByStatusAndDhCompraGreaterThanEqualAndDhCompraLessThan(StatusIngresso status,
                                                                      LocalDateTime inicio,
                                                                      LocalDateTime fim);

    @Query("""
            select coalesce(sum(i.precoPago), 0) from Ingresso i
            where i.lote.jogo.id = :jogoId and i.status = :status
            """)
    BigDecimal contaReceitaPorJogo(@Param("jogoId") Long jogoId, @Param("status") StatusIngresso status);

}


