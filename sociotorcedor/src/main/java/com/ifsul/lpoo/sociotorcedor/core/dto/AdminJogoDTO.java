package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Linha da lista de jogos do painel do admin: situação dos lotes e das vendas. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminJogoDTO {

    private JogoDTO jogo;
    private int lotes;
    private int setoresNoEstadio;
    private Long vendidos;
    private BigDecimal ocupacao;
    private BigDecimal receita;
    private boolean encerrado;

    public boolean isLotesCompletos() {
        return setoresNoEstadio > 0 && lotes >= setoresNoEstadio;
    }

}
