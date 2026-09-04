package com.ifsul.lpoo.sociotorcedor.core.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CategoriaSocio {

    CAT1(1, "cat1");

    private Integer codigo;
    private String descricao;

}
