package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.AssociadoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.BeneficioSocioRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.CategoriaSocioRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.HistoricoCategoriaRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.BeneficioSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.CategoriaSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.exception.SocioTorcedorException;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.base.BeneficioSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.HistoricoCategoria;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocioTorcedorService {

    private final AssociadoRepository associadoRepository;
    private final CategoriaSocioRepository categoriaSocioRepository;
    private final HistoricoCategoriaRepository historicoCategoriaRepository;
    private final BeneficioSocioRepository beneficioSocioRepository;

    /** Categorias que o usuário pode contratar (exclui "não associado"), cada uma com seus benefícios. */
    public List<CategoriaSocioDTO> listarCategoriasDisponiveis() {
        List<CategoriaSocio> categorias = categoriaSocioRepository
                .findByHierarquiaGreaterThanOrderByHierarquiaAsc(CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO);

        // uma consulta só para todos os benefícios, agrupados depois pelo id da categoria
        Map<Long, List<BeneficioSocio>> beneficiosPorCategoria = beneficioSocioRepository
                .findByCategoriaIdIn(categorias.stream().map(CategoriaSocio::getId).toList())
                .stream()
                .collect(Collectors.groupingBy(b -> b.getCategoria().getId()));

        return categorias.stream()
                .map(c -> DTOFactory.createCategoriaSocioDTO(c, beneficiosPorCategoria.getOrDefault(c.getId(), List.of())))
                .toList();
    }

    /** Benefícios de uma categoria (para mostrar o que o sócio atual já recebe). */
    public List<BeneficioSocioDTO> listarBeneficios(Long categoriaId) {
        return beneficioSocioRepository.findByCategoriaIdIn(List.of(categoriaId)).stream()
                .map(DTOFactory::createBeneficioSocioDTO)
                .toList();
    }

    @Transactional
    public void tornarseSocio(Long categoriaId, Usuario usuario) {
        Associado associado = usuario.getAssociado();

        if (!associado.getCategoria().isNaoAssociado()) {
            throw new SocioTorcedorException("Sócio torcedor já está associado");
        }

        CategoriaSocio nova = categoriaSocioRepository.findById(categoriaId)
                .orElseThrow(() -> new SocioTorcedorException("Categoria não encontrada"));

        if (nova.isNaoAssociado()) {
            throw new SocioTorcedorException("Categoria inválida para associação");
        }

        trocarCategoria(associado, nova, LocalDate.now());
        atualizaContextoSeguranca(usuario);
    }

    @Transactional
    public void revogarAssociacao(Usuario usuario) {
        Associado associado = usuario.getAssociado();

        if (associado.getCategoria().isNaoAssociado()) {
            throw new SocioTorcedorException("Sócio já não está associado a nenhuma categoria");
        }

        CategoriaSocio naoAssociado = categoriaSocioRepository
                .findByHierarquia(CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO)
                .orElseThrow(() -> new SocioTorcedorException("Categoria inicial (não associado) não está cadastrada"));

        trocarCategoria(associado, naoAssociado, null);
        atualizaContextoSeguranca(usuario);
    }

    /** Altera a categoria, a data de adesão e registra a mudança no histórico. */
    private void trocarCategoria(Associado associado, CategoriaSocio nova, LocalDate dataAdesao) {
        HistoricoCategoria historico = new HistoricoCategoria();
        historico.setAssociado(associado);
        historico.setCategoriaAnterior(associado.getCategoria());
        historico.setCategoriaNova(nova);
        historico.setData(LocalDate.now());

        associado.setCategoria(nova);
        associado.setDataAdesao(dataAdesao);

        associadoRepository.save(associado);
        historicoCategoriaRepository.save(historico);
    }

    private void atualizaContextoSeguranca(Usuario usuario) {
        try {
            Authentication currAuth = SecurityContextHolder.getContext().getAuthentication();
            assert currAuth != null;

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    usuario,
                    currAuth.getCredentials(),
                    currAuth.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } catch (Exception e) {
            log.warn("Não foi possível atualizar contexto de segurança do usuário");
        }
    }
}
