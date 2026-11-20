package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaSocioDTO {

    private Long id;
    private String nome;
    private BigDecimal mensalidade;
    private BigDecimal descontoIngresso;
    private Integer hierarquia;
    private List<BeneficioSocioDTO> beneficios;

}
