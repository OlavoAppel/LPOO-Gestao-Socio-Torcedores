package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Cabeçalho de coluna da escada de preços: uma categoria (inclusive "Não associado"). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaColunaDTO {

    private String nome;
    private Integer hierarquia;
    private boolean suaCategoria;

}
