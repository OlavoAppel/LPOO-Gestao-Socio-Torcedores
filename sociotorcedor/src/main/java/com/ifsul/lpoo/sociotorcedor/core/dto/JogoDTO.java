package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class JogoDTO {
    private String diaJogo;
    private String mesJogo;
    private String horaJogo;
    private String nomeEstadio;
    private String campeonato;
    private TimeDTO casa;
    private TimeDTO fora;


}
