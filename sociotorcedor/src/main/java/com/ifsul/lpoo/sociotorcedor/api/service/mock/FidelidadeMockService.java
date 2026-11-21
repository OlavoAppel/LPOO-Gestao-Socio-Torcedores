package com.ifsul.lpoo.sociotorcedor.api.service.mock;

import com.ifsul.lpoo.sociotorcedor.api.service.FidelidadeService;
import com.ifsul.lpoo.sociotorcedor.core.dto.MovimentoPontosDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.PontosDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.RecompensaDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * SIMULA o programa de pontos: devolve o mesmo saldo e extrato de demonstração para qualquer sócio
 * (marcado com previa = true, e a tela avisa) e um catálogo em que nada pode ser resgatado ainda.
 *
 * <p>Para virar real faltaria no backend: uma entidade de movimento de pontos (associado, data, origem, pontos);
 * as regras de pontuação (mensalidade paga em dia, presença no jogo, compras na loja); o catálogo de
 * recompensas com custo e estoque; e o resgate, que é uma escrita (debita o saldo e registra a retirada).
 */
@Service
public class FidelidadeMockService implements FidelidadeService {

    @Override
    public PontosDTO resumoDo(Usuario usuario) {
        LocalDate hoje = LocalDate.now();
        List<MovimentoPontosDTO> extrato = List.of(
                new MovimentoPontosDTO(hoje.minusDays(3), "Mensalidade paga em dia", 120),
                new MovimentoPontosDTO(hoje.minusDays(9), "Presença no estádio", 200),
                new MovimentoPontosDTO(hoje.minusDays(16), "Compra na loja oficial", 60),
                new MovimentoPontosDTO(hoje.minusDays(33), "Mensalidade paga em dia", 120),
                new MovimentoPontosDTO(hoje.minusDays(40), "Presença no estádio", 200));
        List<RecompensaDTO> recompensas = List.of(
                new RecompensaDTO("Copo da torcida", "Copo térmico com o brasão em mosaico.", 800, false),
                new RecompensaDTO("Visita ao vestiário em dia de jogo", "Para você e um acompanhante, antes do aquecimento.", 3000, false),
                new RecompensaDTO("Camisa com o seu nome", "Modelo da temporada, com nome e número à sua escolha.", 5000, false));
        return new PontosDTO(true, 1240, extrato, recompensas);
    }

}
