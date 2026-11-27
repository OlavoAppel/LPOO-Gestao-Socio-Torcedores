package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.AssociadoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.IngressoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.LoteIngressoRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoCategoriaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoJogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

/**
 * Cada metodo público monta UM DTO, chamando várias consultas pequenas.
 * Assim cada informação pode virar uma página própria sem depender das outras.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {

    private static final StatusIngresso STATUS_PAGO = StatusIngresso.PAGO;

    private final AssociadoRepository associadoRepository;
    private final IngressoRepository ingressoRepository;
    private final LoteIngressoRepository loteIngressoRepository;
    private final JogoRepository jogoRepository;

    public DashboardInfoDTO getInformacoesDashboard() {
        YearMonth mes = YearMonth.now();
        LocalDate inicioData = mes.atDay(1);
        LocalDate fimData = mes.atEndOfMonth();
        LocalDateTime inicio = inicioData.atStartOfDay();
        LocalDateTime fim = mes.plusMonths(1).atDay(1).atStartOfDay(); // fim exclusivo

        DashboardInfoDTO dto = new DashboardInfoDTO();

        dto.setAssociadosTotal(
                associadoRepository.countByCategoriaHierarquiaGreaterThan(CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO));

        dto.setNovosAssociadosMesAtual(
                associadoRepository.countByCategoriaHierarquiaGreaterThanAndDataAdesaoBetween(
                        CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO, inicioData, fimData));

        dto.setGanhosTotais(ingressoRepository.somarReceitaPorStatus(STATUS_PAGO).add(associadoRepository.somarMesalidadeAssociados()));

        dto.setReceitaIngressos(ingressoRepository.somarReceitaPorStatusNoPeriodo(STATUS_PAGO, inicio, fim));

        dto.setIngressosVendidos(
                ingressoRepository.countByStatusAndDhCompraGreaterThanEqualAndDhCompraLessThan(STATUS_PAGO, inicio, fim));

        return dto;
    }

    public List<DashboardInfoCategoriaDTO> getInformacoesCategorias() {
        YearMonth mes = YearMonth.now();
        return associadoRepository.resumoPorCategoria(mes.atDay(1), mes.atEndOfMonth());
    }

    /** Um DTO por jogo futuro, numa única consulta. */
    public List<DashboardInfoJogoDTO> getInformacoesJogos() {
        return jogoRepository.resumoPorJogo(LocalDateTime.now(), STATUS_PAGO);
    }

    public DashboardInfoJogoDTO getInformacaoJogos(Long jogoId) {
        DashboardInfoJogoDTO dto = new DashboardInfoJogoDTO();

        Long vendidos = loteIngressoRepository.contaIngressosVendidosPorJogo(jogoId, STATUS_PAGO);
        Long capacidade = loteIngressoRepository.somaCapacidadePorJogo(jogoId);

        dto.setIngressosVendidos(vendidos);
        dto.setReceitaPorJogo(ingressoRepository.contaReceitaPorJogo(jogoId, STATUS_PAGO));
        dto.setOcupacaoJogoPorcentagem(DashboardInfoJogoDTO.calcularPorcentagem(vendidos, capacidade));

        return dto;
    }
}
