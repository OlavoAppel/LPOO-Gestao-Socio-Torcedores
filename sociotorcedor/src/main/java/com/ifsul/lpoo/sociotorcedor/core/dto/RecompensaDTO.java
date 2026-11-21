package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Item do catálogo de recompensas do programa de pontos. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecompensaDTO {

    private String nome;
    private String descricao;
    private int custo;
    /** Falso enquanto o resgate não existir no backend (a tela mostra o botão desabilitado). */
    private boolean resgatavel;

}
