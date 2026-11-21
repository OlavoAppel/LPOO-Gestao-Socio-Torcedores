package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/** Um lançamento no extrato de pontos. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentoPontosDTO {

    private LocalDate data;
    private String descricao;
    private int pontos;

}
