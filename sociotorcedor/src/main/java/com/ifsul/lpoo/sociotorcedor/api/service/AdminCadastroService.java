package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.TimeRepository;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroJogoForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Time;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCadastroService {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Transactional
    public Jogo cadastrarJogo(CadastroJogoForm form) {
        Time casa = timeRepository.findById(form.getCasaId())
                .orElseThrow(() -> new IllegalArgumentException("Time da casa não encontrado"));
        Time fora = timeRepository.findById(form.getForaId())
                .orElseThrow(() -> new IllegalArgumentException("Time visitante não encontrado"));

        Jogo jogo = new Jogo();
        jogo.setCasa(casa);
        jogo.setFora(fora);
        jogo.setDhJogo(form.getDhJogo());
        jogo.setCampeonato(form.getCampeonato());
        //jogo.setNomeEstadio(form.getNomeEstadio());

        return jogoRepository.save(jogo);
    }



}
