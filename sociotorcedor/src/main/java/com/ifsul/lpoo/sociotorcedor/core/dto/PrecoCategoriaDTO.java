package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Quanto uma categoria de sócio paga num setor: o preço do lote com o desconto da categoria. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrecoCategoriaDTO {

    private Long categoriaId;
    private String categoria;
    private Integer hierarquia;
    private BigDecimal preco;
    /** Categoria do torcedor logado (na tela, a coluna dele fica em destaque). */
    private boolean suaCategoria;

}
