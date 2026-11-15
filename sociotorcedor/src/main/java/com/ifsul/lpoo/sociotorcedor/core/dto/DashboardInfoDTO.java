package com.ifsul.lpoo.sociotorcedor.core.dto;

import jakarta.validation.constraints.NegativeOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DashboardInfoDTO {

    private BigDecimal associadosTotal;
    private BigDecimal novosAssociadosMesAnterior;
    private BigDecimal ganhosTotais;
    private List<InformacaoCategoriaDTO> infoCategoriaSocioList;


}
