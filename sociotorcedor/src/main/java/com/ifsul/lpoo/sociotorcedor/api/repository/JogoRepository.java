package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoJogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.jpa.repository.Query;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    // Campeonatos já usados, para sugerir no formulário de cadastro de jogo (só leitura).
    @Query("select distinct j.campeonato from Jogo j where j.campeonato is not null order by j.campeonato")
    List<String> listarCampeonatos();

    List<Jogo> findByDhJogoBetween(LocalDateTime dhInicaio, LocalDateTime dhFim);

    // Cada métrica é uma subquery no SELECT, correlacionada ao jogo da linha (j).
    // O DTO recebe vendidos e capacidade crus e calcula a porcentagem no construtor.
    @Query("""
            select new com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoJogoDTO(
                j.id,
                concat(j.casa.sigla, ' x ', j.fora.sigla),
                (select count(i) from Ingresso i
                    where i.lote.jogo = j and i.status = :status),
                (select coalesce(sum(l.ingressosTotaisLote), 0) from LoteIngresso l
                    where l.jogo = j),
                (select coalesce(sum(i.precoPago), 0) from Ingresso i
                    where i.lote.jogo = j and i.status = :status)
            )
            from Jogo j
            where j.dhJogo >= :desde
            order by j.dhJogo
            """)
    List<DashboardInfoJogoDTO> resumoPorJogo(@Param("desde") LocalDateTime desde,
                                             @Param("status") StatusIngresso status);

}


