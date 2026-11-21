package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoCategoriaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.MembroMosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    // Mosaico da torcida (só leitura): sócios ativos na ordem de adesão, só com id e hierarquia da categoria.
    @Query("""
            select new com.ifsul.lpoo.sociotorcedor.core.dto.MembroMosaicoDTO(a.id, c.hierarquia)
            from Associado a
            join a.categoria c
            where c.hierarquia > :naoAssociado
            order by a.dataAdesao asc nulls last, a.id asc
            """)
    List<MembroMosaicoDTO> listarMembrosDoMosaico(@Param("naoAssociado") Integer naoAssociado);

    // Query derivada: o Spring monta o SQL só pelo nome do método.
    // Lê-se: count where categoria.hierarquia > :hierarquia
    long countByCategoriaHierarquiaGreaterThan(Integer hierarquia);

    // ... and dataAdesao between :inicio and :fim
    long countByCategoriaHierarquiaGreaterThanAndDataAdesaoBetween(Integer hierarquia, LocalDate inicio, LocalDate fim);

    // @Query com JPQL: necessária quando há agrupamento. O "select new" já devolve o DTO pronto.
    // LEFT JOIN para categorias sem nenhum sócio aparecerem com 0.
    @Query("""
            select new com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoCategoriaDTO(
                c.id,
                c.nome,
                count(a),
                count(case when a.dataAdesao between :inicio and :fim then 1 end))
            from CategoriaSocio c
            left join Associado a on a.categoria = c
            where c.hierarquia > 0
            group by c.id, c.nome, c.hierarquia
            order by c.hierarquia
            """)
    List<DashboardInfoCategoriaDTO> resumoPorCategoria(@Param("inicio") LocalDate inicio,
                                                       @Param("fim") LocalDate fim);

    @Query("""
            select coalesce((sum(c.mensalidade)), 0) from Associado a 
            left join CategoriaSocio c on a.categoria = c
            where c.hierarquia > 0
            """)
    BigDecimal somarMesalidadeAssociados();

}
