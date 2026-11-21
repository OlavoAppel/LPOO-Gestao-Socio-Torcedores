package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** O mosaico da torcida: um quadrado por sócio ativo, na ordem de adesão. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MosaicoDTO {

    private List<PecaMosaicoDTO> pecas;
    private long totalSocios;
    /** Quantos sócios cada quadrado representa (1 enquanto houver poucos sócios). */
    private int sociosPorPeca;
    /** Posição do torcedor logado na ordem de adesão (1 = o mais antigo); nula se ele não é sócio. */
    private Integer suaPosicao;
    private List<NivelMosaicoDTO> niveis;

    public boolean isVazio() {
        return totalSocios == 0;
    }

}
