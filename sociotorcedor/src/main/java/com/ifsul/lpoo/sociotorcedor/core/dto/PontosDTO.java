package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Programa de pontos do sócio (hoje vem de FidelidadeMockService; ver docs/front/dados-ficticios.md). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PontosDTO {

    /** Verdadeiro quando os dados são de demonstração: a tela avisa que é uma prévia. */
    private boolean previa;
    private int saldo;
    private List<MovimentoPontosDTO> extrato;
    private List<RecompensaDTO> recompensas;

}
