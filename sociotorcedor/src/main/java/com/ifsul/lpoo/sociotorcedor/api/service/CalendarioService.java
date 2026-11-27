package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogosDisponiveisDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.MesCalendarioDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Organiza a lista de jogos do ano para as telas: ordena por data e agrupa por mês.
 * Trabalha só sobre os DTOs que o {@link JogoService} já devolve (não consulta o banco).
 */
@Service
public class CalendarioService {

    /// Jogos que ainda vão acontecer, do mais próximo ao mais distante, agrupados por mês.
    public List<MesCalendarioDTO> proximosPorMes(JogosDisponiveisDTO dados, LocalDateTime agora) {
        return agrupar(jogos(dados).stream()
                .filter(j -> !j.getDhJogo().isBefore(agora))
                .sorted(Comparator.comparing(JogoDTO::getDhJogo))
                .toList());
    }

    /// Jogos que já aconteceram, do mais recente ao mais antigo, agrupados por mês.
    public List<MesCalendarioDTO> encerradosPorMes(JogosDisponiveisDTO dados, LocalDateTime agora) {
        return agrupar(jogos(dados).stream()
                .filter(j -> j.getDhJogo().isBefore(agora))
                .sorted(Comparator.comparing(JogoDTO::getDhJogo).reversed())
                .toList());
    }

    /// Os próximos jogos depois de agora, sem o jogo informado (o "próximo jogo" já em destaque).
    public List<JogoDTO> proximos(JogosDisponiveisDTO dados, LocalDateTime agora, Long excetoId, int limite) {
        return jogos(dados).stream()
                .filter(j -> !j.getDhJogo().isBefore(agora))
                .filter(j -> !Objects.equals(j.getId(), excetoId))
                .sorted(Comparator.comparing(JogoDTO::getDhJogo))
                .limit(limite)
                .toList();
    }

    private List<JogoDTO> jogos(JogosDisponiveisDTO dados) {
        if (dados == null || dados.getJogosList() == null) {
            return List.of();
        }
        return dados.getJogosList().stream().filter(j -> j.getDhJogo() != null).toList();
    }

    private List<MesCalendarioDTO> agrupar(List<JogoDTO> ordenados) {
        Map<YearMonth, List<JogoDTO>> porMes = new LinkedHashMap<>();
        for (JogoDTO jogo : ordenados) {
            porMes.computeIfAbsent(YearMonth.from(jogo.getDhJogo()), m -> new ArrayList<>()).add(jogo);
        }
        return porMes.entrySet().stream()
                .map(e -> new MesCalendarioDTO(e.getKey().getYear(), e.getKey().getMonthValue(), e.getValue()))
                .toList();
    }

}
