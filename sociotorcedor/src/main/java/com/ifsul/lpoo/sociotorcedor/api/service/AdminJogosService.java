package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.AdminJogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoJogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Dados de leitura do painel do admin: todos os jogos com a situação dos lotes e das vendas. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminJogosService {

    /** Data antiga o bastante para o resumo de vendas incluir também os jogos já realizados. */
    private static final LocalDateTime DESDE_SEMPRE = LocalDateTime.of(1900, 1, 1, 0, 0);

    private final JogoRepository jogoRepository;

    public List<AdminJogoDTO> listarParaPainel() {
        LocalDateTime agora = LocalDateTime.now();
        // mesma consulta das métricas (ingressos PAGO), só que desde sempre
        Map<Long, DashboardInfoJogoDTO> resumo = jogoRepository.resumoPorJogo(DESDE_SEMPRE, StatusIngresso.PAGO).stream()
                .collect(Collectors.toMap(DashboardInfoJogoDTO::getJogoId, Function.identity(), (a, b) -> a));

        return jogoRepository.findAll(Sort.by("dhJogo")).stream()
                .map(jogo -> DTOFactory.createAdminJogoDTO(jogo, resumo.get(jogo.getId()), agora))
                .toList();
    }

    public List<String> listarCampeonatos() {
        return jogoRepository.listarCampeonatos();
    }

}
