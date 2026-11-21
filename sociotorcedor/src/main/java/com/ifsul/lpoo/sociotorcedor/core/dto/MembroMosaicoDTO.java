package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Linha da consulta do mosaico: um sócio ativo e a hierarquia da categoria dele. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembroMosaicoDTO {

    private Long associadoId;
    private Integer hierarquia;

}
