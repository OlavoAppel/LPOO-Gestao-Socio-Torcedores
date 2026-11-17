package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardInfoJogoDTO {

    private BigDecimal ocupacaoJogoPorcentagem;
    private BigDecimal receitaPorJogo;
    private Long ingressosVendidos;

}
