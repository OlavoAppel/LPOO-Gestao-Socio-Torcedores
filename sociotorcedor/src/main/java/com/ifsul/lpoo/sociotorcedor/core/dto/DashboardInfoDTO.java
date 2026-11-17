package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardInfoDTO {

    private Long associadosTotal;
    private Long novosAssociadosMesAtual;
    private BigDecimal ganhosTotais;
    private BigDecimal receitaIngressos; // utilizando mês atual
    private Long ingressosVendidos; // utilizando mês atual

}
