package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Uma linha da tabela de comparação de planos: um benefício e quais categorias o têm. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinhaBeneficioDTO {

    private String nome;
    private String descricao;
    /** Na mesma ordem das categorias da tabela. */
    private List<Boolean> porCategoria;

}
