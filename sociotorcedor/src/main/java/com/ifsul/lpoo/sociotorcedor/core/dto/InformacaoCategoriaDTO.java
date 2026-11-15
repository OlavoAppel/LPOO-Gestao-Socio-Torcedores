package com.ifsul.lpoo.sociotorcedor.core.dto;

import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class InformacaoCategoriaDTO {

    private Categoria categoria;
    private BigDecimal totalAssociados;
    private BigDecimal novosSociosMesAnterior;


}
