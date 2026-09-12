package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class JogosDisponiveisDTO {

    private JogoDTO proximoJogo;
    private List<JogoDTO> jogosList;

    public void addJogo(JogoDTO jogo){
        this.jogosList.add(jogo);
    }
}
