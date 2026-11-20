package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.*;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroJogoForm;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroLoteForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCadastroService {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private SetorRepository setorRepository;
    @Autowired
    private LoteIngressoRepository loteIngressoRepository;
    @Autowired
    private EstadioRepository estadioRepository;

    @Transactional
    public Jogo cadastrarJogo(CadastroJogoForm form) {
        Time casa = timeRepository.findById(form.getCasaId())
                .orElseThrow(() -> new IllegalArgumentException("Time da casa não encontrado"));
        Time fora = timeRepository.findById(form.getForaId())
                .orElseThrow(() -> new IllegalArgumentException("Time visitante não encontrado"));
        Estadio estadio = estadioRepository.findById(form.getEstadioId())
                .orElseThrow(() -> new IllegalArgumentException("Estadio não encontrado"));

        Jogo jogo = new Jogo();
        jogo.setCasa(casa);
        jogo.setFora(fora);
        jogo.setDhJogo(form.getDhJogo());
        jogo.setCampeonato(form.getCampeonato());
        jogo.setEstadio(estadio);

        return jogoRepository.save(jogo);
    }

    @Transactional
    public void cadastrarLote(Long jogoId, CadastroLoteForm form) {
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new IllegalArgumentException("Jogo não encontrado"));
        SetorEstadio setor = setorRepository.findById(form.getSetorId())
                .orElseThrow(() -> new IllegalArgumentException("Setor não encontrado"));

        if (loteIngressoRepository.existsByJogoIdAndSetorId(jogoId, setor.getId())) {
            throw new IllegalArgumentException("Este setor já possui lote cadastrado para o jogo");
        }
        if (form.getEstoque() > setor.getCapacidadeTotal()) {
            throw new IllegalArgumentException(
                    "O estoque excede a capacidade do setor (" + setor.getCapacidadeTotal() + ")");
        }

        LoteIngresso lote = new LoteIngresso();
        lote.setJogo(jogo);
        lote.setSetor(setor);
        lote.setPrecoBase(form.getPrecoBase());
        lote.setIngressoDisponiveis(form.getEstoque());
        lote.setIngressosTotaisLote(form.getEstoque());
        loteIngressoRepository.save(lote);

    }



}
