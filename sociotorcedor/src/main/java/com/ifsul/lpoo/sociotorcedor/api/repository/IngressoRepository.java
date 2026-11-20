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

    // Ingressos de um usuário, já com jogo/times/estádio/setor carregados (evita lazy na view).
    @Query("""
            select i from Ingresso i
            join fetch i.lote l
            join fetch l.jogo j
            join fetch j.casa
            join fetch j.fora
            join fetch j.estadio
            left join fetch l.setor
            where i.associado.id = (select u.associado.id from Usuario u where u.username = :username)
            order by j.dhJogo desc, i.dhCompra desc
            """)
    java.util.List<Ingresso> buscarDoUsuario(@Param("username") String username);

    long countByStatusAndDhCompraGreaterThanEqualAndDhCompraLessThan(StatusIngresso status,
                                                                      LocalDateTime inicio,
                                                                      LocalDateTime fim);

    @Query("""
            select coalesce(sum(i.precoPago), 0) from Ingresso i
            where i.lote.jogo.id = :jogoId and i.status = :status
            """)
    BigDecimal contaReceitaPorJogo(@Param("jogoId") Long jogoId, @Param("status") StatusIngresso status);

}


