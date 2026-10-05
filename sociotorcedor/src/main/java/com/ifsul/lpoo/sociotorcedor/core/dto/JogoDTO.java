package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class JogoDTO {
    private Long id;
    private LocalDateTime dhJogo;
    private String nomeEstadio;
    private String campeonato;
    private TimeDTO casa;
    private TimeDTO fora;
}
