package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaSocioRepository extends JpaRepository<CategoriaSocio, Long> {

    Optional<CategoriaSocio> findByHierarquia(Integer hierarquia);

    List<CategoriaSocio> findByHierarquiaGreaterThanOrderByHierarquiaAsc(Integer hierarquia);
}
