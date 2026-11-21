package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.CategoriaSocioRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDetalheDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.LoteIngressoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.SetorOfertaDTO;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Monta a página de um jogo e a escada de preços (o que cada categoria paga em cada setor). Só leitura.
 *
 * <p>Não decide nada sobre venda: a validade de cada lote vem de {@link LoteIngressoService#findLotes},
 * que roda a mesma cadeia de validação da compra, e os preços usam {@code CategoriaSocio.aplicarDescontoIngresso},
 * o mesmo cálculo aplicado em {@code comprarIngresso}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JogoDetalheService {

    private final JogoRepository jogoRepository;
    private final CategoriaSocioRepository categoriaSocioRepository;
    private final LoteIngressoService loteIngressoService;
    private final JogoService jogoService;

    public Optional<JogoDetalheDTO> detalhar(Long jogoId, Usuario usuario) {
        Optional<Jogo> encontrado = jogoRepository.findById(jogoId);
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }
        Jogo jogo = encontrado.get();
        LocalDateTime agora = LocalDateTime.now();

        ContextoCompra contexto = new ContextoCompra(usuario, agora, jogoId, null);
        Map<Long, LoteIngressoDTO> situacao = loteIngressoService.findLotes(contexto).stream()
                .collect(Collectors.toMap(LoteIngressoDTO::id, l -> l, (a, b) -> a));

        List<CategoriaSocio> categorias = categoriaSocioRepository.findAll(Sort.by("hierarquia"));
        boolean temCadastroDeTorcedor = usuario != null && usuario.getAssociado() != null;
        CategoriaSocio sua = temCadastroDeTorcedor ? usuario.getAssociado().getCategoria() : null;

        List<SetorOfertaDTO> setores = jogo.getLoteIngressoList().stream()
                .map(lote -> DTOFactory.createSetorOfertaDTO(lote, situacao.get(lote.getId()), categorias, sua, agora))
                .sorted(Comparator.comparing(SetorOfertaDTO::getPrecoBase, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        return Optional.of(DTOFactory.createJogoDetalheDTO(jogo, setores, categorias, sua, temCadastroDeTorcedor, agora));
    }

    /** Escada de preços do próximo jogo, usada como exemplo concreto na comparação de planos. */
    public Optional<JogoDetalheDTO> detalharProximo(Usuario usuario) {
        JogoDTO proximo = jogoService.findJogosAno().getProximoJogo();
        return proximo == null ? Optional.empty() : detalhar(proximo.getId(), usuario);
    }

}
