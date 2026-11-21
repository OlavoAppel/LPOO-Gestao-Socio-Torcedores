package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Um quadrado do mosaico da torcida. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PecaMosaicoDTO {

    /** Tom do quadrado: 1, 2 ou 3, na ordem das categorias (Bronze, Prata, Ouro no seeder). */
    private int nivel;
    /** O quadrado do torcedor logado. */
    private boolean seu;

}
