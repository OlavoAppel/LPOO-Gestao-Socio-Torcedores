package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Legenda do mosaico: uma categoria, o tom usado para ela e quantos sócios ela tem. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NivelMosaicoDTO {

    private String categoria;
    private Integer hierarquia;
    private int nivel;
    private long total;

}
