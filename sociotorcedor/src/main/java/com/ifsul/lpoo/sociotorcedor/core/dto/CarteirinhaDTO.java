package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/** Dados da carteirinha digital (todos reais, vindos do cadastro do sócio e do mosaico). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarteirinhaDTO {

    private String nome;
    private String categoria;
    /** Tom da categoria no mosaico (1 a 3). */
    private int nivel;
    private Long numero;
    private LocalDate socioDesde;
    private Integer posicaoMosaico;
    private long totalMosaico;

}
