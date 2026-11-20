package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardInfoJogoDTO {

    private Long jogoId;
    private String titulo; // ex.: "SPFC x GRE"
    private BigDecimal ocupacaoJogoPorcentagem;
    private BigDecimal receitaPorJogo;
    private Long ingressosVendidos;

    /** Usado pelo "select new" da JpQL: recebe os números crus e calcula a ocupação aqui. */
    public DashboardInfoJogoDTO(Long jogoId, String titulo, Long vendidos, Long capacidade, BigDecimal receita) {
        this(jogoId, titulo, calcularPorcentagem(vendidos, capacidade), receita, vendidos);
    }

    /** vendidos / capacidade * 100, com 1 casa decimal. Devolve 0 se não houver capacidade. */
    public static BigDecimal calcularPorcentagem(Long parte, Long total) {
        if (total == null || total == 0 || parte == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(parte)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP);
    }

}
