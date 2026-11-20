package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.BeneficioSocio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BeneficioSocioRepository extends JpaRepository<BeneficioSocio, Long> {

    // Lê-se: where categoria.id in (:ids). Uma única consulta para todas as categorias.
    List<BeneficioSocio> findByCategoriaIdIn(Collection<Long> categoriaIds);

}
