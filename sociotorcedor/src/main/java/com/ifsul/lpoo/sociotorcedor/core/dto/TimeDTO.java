package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class TimeDTO {
    private String sigla;
    private String escudoPath;
    /** Nome completo, para títulos e texto alternativo do escudo. */
    private String nome;
}
