package com.ifsul.lpoo.sociotorcedor.core.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CategoriaSocio {

    NAO_ASSOCIADO(1, "Não associado");

    private Integer codigo;
    private String descricao;

}
