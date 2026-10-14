package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoteIngressoRepository extends JpaRepository<LoteIngresso, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE LoteIngresso l SET l.ingressoDisponiveis = l.ingressoDisponiveis - :quantidade WHERE l.id = :id")
    int decrementarIngressosDisponiveis(@Param("id") Long id, @Param("quantidade") int quantidade);

    List<LoteIngresso> findAllByJogo_Id(Long jogoId);
}
