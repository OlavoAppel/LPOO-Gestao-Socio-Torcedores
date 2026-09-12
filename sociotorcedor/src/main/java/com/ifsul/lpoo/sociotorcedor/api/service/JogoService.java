package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogosDisponiveisDTO;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JogoService {

    public JogosDisponiveisDTO findJogosAno(){
        int anoAtual = LocalDateTime.now().getYear();
        LocalDateTime dhInicio = LocalDateTime.of(anoAtual, 1, 1, 0, 0);
        LocalDateTime dhFim = LocalDateTime.of(anoAtual, 12, 31, 23, 59);
        LocalDateTime dhAtual = LocalDateTime.now();

        List<Jogo> jogosAnuais = InjectionProvider.getJogoRepository().findByDhJogoBetween(dhInicio, dhFim);
        JogosDisponiveisDTO jogosDisponiveis = new JogosDisponiveisDTO();
        Jogo proximoJogo = null;

        for(Jogo jogo : jogosAnuais){
            JogoDTO jogoDTO = DTOFactory.createJogoDTO(jogo);
            jogosDisponiveis.addJogo(jogoDTO);

            proximoJogo = resolveProximoJogo(jogo, proximoJogo, dhAtual);
        }

        if(proximoJogo != null) jogosDisponiveis.setProximoJogo(DTOFactory.createJogoDTO(proximoJogo));
        return jogosDisponiveis;
    }

    private Jogo resolveProximoJogo(Jogo jogo, Jogo proximoJogo, LocalDateTime dhAtual) {
        if(dhAtual.isAfter(jogo.getDhJogo())) return proximoJogo;

        if (proximoJogo == null) {
            return jogo;
        }

        Duration duracaoJogoAtual = Duration.between(dhAtual, jogo.getDhJogo());
        Duration duracaoProximoJogo = Duration.between(dhAtual, proximoJogo.getDhJogo());

        return (duracaoJogoAtual.compareTo(duracaoProximoJogo) < 0 ? jogo : proximoJogo);
    }

}
