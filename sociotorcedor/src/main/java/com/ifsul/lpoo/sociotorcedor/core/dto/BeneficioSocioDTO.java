package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BeneficioSocioDTO {

    private String nome;
    private String descricao;
    private BigDecimal descontoIngresso;
    private BigDecimal descontoBrindes;

}
