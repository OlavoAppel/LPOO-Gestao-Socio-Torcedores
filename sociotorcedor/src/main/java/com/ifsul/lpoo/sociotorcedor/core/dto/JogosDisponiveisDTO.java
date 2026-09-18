package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class JogosDisponiveisDTO {

    private JogoDTO proximoJogo;
    private List<JogoDTO> jogosList;

    public void addJogo(JogoDTO jogo){
        if(this.jogosList == null) this.jogosList = new ArrayList<>();
        this.jogosList.add(jogo);
    }

}
