package com.ifsul.lpoo.sociotorcedor.core.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CategoriaSocio {

    NAO_ASSOCIADO(1, "Não associado", new BigDecimal("0.00")),
    SOCIO1TESTE(2, "Teste", new BigDecimal("2.50"));

    private Integer codigo;
    private String descricao;
    private BigDecimal preco;

}
