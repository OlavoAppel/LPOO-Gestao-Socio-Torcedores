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

    // Ingressos de um usuário do mais recente para o mais antigo pela data da compra (tela de compra concluída).
    @Query("""
            select i from Ingresso i
            join fetch i.lote l
            join fetch l.jogo j
            join fetch j.casa
            join fetch j.fora
            join fetch j.estadio
            left join fetch l.setor
            where i.associado.id = (select u.associado.id from Usuario u where u.username = :username)
            order by i.dhCompra desc, i.id desc
            """)
    java.util.List<Ingresso> buscarDoUsuarioPorCompraRecente(@Param("username") String username,
                                                             org.springframework.data.domain.Limit limite);

    @Query("select coalesce(sum(i.precoPago), 0) from Ingresso i where i.status = :status")
    BigDecimal somarReceitaPorStatus(@Param("status") StatusIngresso status);

    @Query("""
            select coalesce(sum(i.precoPago), 0) from Ingresso i
            where i.status = :status and i.dhCompra >= :inicio and i.dhCompra < :fim
            """)
    BigDecimal somarReceitaPorStatusNoPeriodo(@Param("status") StatusIngresso status,
                                              @Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim);

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

    long countByAssociado_IdAndLote_Jogo_IdAndStatusIn(Long associadoId, Long jogoId,
                                                       java.util.Collection<StatusIngresso> status);

}


