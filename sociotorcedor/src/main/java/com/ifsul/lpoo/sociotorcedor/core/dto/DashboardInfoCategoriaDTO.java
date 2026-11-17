package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardInfoCategoriaDTO {

    private Long categoriaId;
    private String nomeCategoria;
    private Long totalAssociados;
    private Long novosAssociadosMesAtual;

}
