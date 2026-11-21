package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/** Uma troca de categoria do sócio (montado direto pela consulta do repositório). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoCategoriaDTO {

    private LocalDate data;
    private String de;
    private String para;
    private Integer hierarquiaPara;

}
