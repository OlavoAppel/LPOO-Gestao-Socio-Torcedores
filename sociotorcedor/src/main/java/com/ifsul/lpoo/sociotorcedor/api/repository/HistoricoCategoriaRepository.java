package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.dto.HistoricoCategoriaDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.base.HistoricoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricoCategoriaRepository extends JpaRepository<HistoricoCategoria, Long> {

    // Trocas de categoria de um sócio, da mais recente para a mais antiga (só leitura, para "Minha associação").
    @Query("""
            select new com.ifsul.lpoo.sociotorcedor.core.dto.HistoricoCategoriaDTO(h.data, anterior.nome, nova.nome, nova.hierarquia)
            from HistoricoCategoria h
            join h.categoriaAnterior anterior
            join h.categoriaNova nova
            where h.associado.id = :associadoId
            order by h.data desc, h.id desc
            """)
    List<HistoricoCategoriaDTO> buscarDoAssociado(@Param("associadoId") Long associadoId);

}
