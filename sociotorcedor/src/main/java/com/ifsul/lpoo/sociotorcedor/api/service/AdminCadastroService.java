package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.*;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroJogoForm;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroLoteForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Time> listarTimes() {
        return timeRepository.findAll();
    }

    public List<Estadio> listarEstadios() {
        return estadioRepository.findAll();
    }

    public List<SetorEstadio> listarSetores() {
        return setorRepository.findAll();
    }

    public Jogo buscarJogo(Long jogoId) {
        return jogoRepository.findById(jogoId)
                .orElseThrow(() -> new IllegalArgumentException("Jogo não encontrado"));
    }

    public List<LoteIngresso> listarLotes(Long jogoId) {
        return loteIngressoRepository.findAllByJogo_Id(jogoId);
    }

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

        validarJanelaDeVenda(jogo, form);

        LoteIngresso lote = new LoteIngresso();
        lote.setJogo(jogo);
        lote.setSetor(setor);
        lote.setPrecoBase(form.getPrecoBase());
        lote.setIngressoDisponiveis(form.getEstoque());
        lote.setIngressosTotaisLote(form.getEstoque());
        lote.setDhInicioVenda(form.getDhInicioVenda());
        lote.setDhTerminoVendaExclusiva(form.getDhTerminoVendaExclusiva());
        loteIngressoRepository.save(lote);

    }

    /** A venda começa antes do jogo, e a fase exclusiva (se houver) fica dentro da venda: início < término ≤ jogo. */
    private void validarJanelaDeVenda(Jogo jogo, CadastroLoteForm form) {
        LocalDateTime inicio = form.getDhInicioVenda();
        LocalDateTime terminoExclusiva = form.getDhTerminoVendaExclusiva();

        if (!inicio.isBefore(jogo.getDhJogo())) {
            throw new IllegalArgumentException("O início da venda deve ser antes do jogo");
        }
        if (terminoExclusiva != null) {
            if (!terminoExclusiva.isAfter(inicio)) {
                throw new IllegalArgumentException("O término da venda exclusiva deve ser depois do início da venda");
            }
            if (terminoExclusiva.isAfter(jogo.getDhJogo())) {
                throw new IllegalArgumentException("O término da venda exclusiva não pode ser depois do jogo");
            }
        }
    }



}
