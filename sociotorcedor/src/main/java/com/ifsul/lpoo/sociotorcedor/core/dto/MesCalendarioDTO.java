package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Um mês do calendário com os jogos dele, já em ordem. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MesCalendarioDTO {

    private int ano;
    private int mes;
    private List<JogoDTO> jogos;

    /** Âncora usada no índice de meses da página de jogos. */
    public String getAncora() {
        return "mes-" + ano + "-" + mes;
    }

}
