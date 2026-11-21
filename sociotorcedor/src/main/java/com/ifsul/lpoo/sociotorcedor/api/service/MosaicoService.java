package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.AssociadoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.CategoriaSocioRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.MembroMosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.MosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.NivelMosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.PecaMosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mosaico da torcida: um quadrado por sócio ativo, na ordem de adesão, no tom da categoria dele. Só leitura.
 *
 * <p>Os tons seguem a ordem das categorias pagas (a 1ª, a 2ª e a 3ª da hierarquia); a partir da 4ª,
 * todas usam o tom mais forte. Com muitos sócios, cada quadrado passa a representar vários deles,
 * para o mosaico não crescer sem limite.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MosaicoService {

    static final int MAXIMO_DE_QUADRADOS = 400;
    private static final int TONS = 3;

    private final AssociadoRepository associadoRepository;
    private final CategoriaSocioRepository categoriaSocioRepository;

    /** Mosaico visto pelo usuário informado (pode ser nulo); o quadrado dele vem marcado. */
    public MosaicoDTO montar(Usuario usuario) {
        Long associadoId = usuario != null && usuario.getAssociado() != null ? usuario.getAssociado().getId() : null;

        List<CategoriaSocio> categorias = categoriaSocioRepository
                .findByHierarquiaGreaterThanOrderByHierarquiaAsc(CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO);
        Map<Integer, Integer> tomPorHierarquia = new HashMap<>();
        for (int i = 0; i < categorias.size(); i++) {
            tomPorHierarquia.put(categorias.get(i).getHierarquia(), Math.min(i + 1, TONS));
        }

        List<MembroMosaicoDTO> membros = associadoRepository.listarMembrosDoMosaico(CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO);
        int total = membros.size();
        int porQuadrado = Math.max(1, (int) Math.ceil(total / (double) MAXIMO_DE_QUADRADOS));

        List<PecaMosaicoDTO> pecas = new ArrayList<>();
        Integer suaPosicao = null;
        for (int inicio = 0; inicio < total; inicio += porQuadrado) {
            int fim = Math.min(inicio + porQuadrado, total);
            boolean seu = false;
            for (int i = inicio; i < fim; i++) {
                if (associadoId != null && Objects.equals(associadoId, membros.get(i).getAssociadoId())) {
                    seu = true;
                    suaPosicao = i + 1;
                }
            }
            pecas.add(new PecaMosaicoDTO(tomPorHierarquia.getOrDefault(membros.get(inicio).getHierarquia(), 1), seu));
        }

        Map<Integer, Long> porHierarquia = membros.stream()
                .collect(Collectors.groupingBy(MembroMosaicoDTO::getHierarquia, Collectors.counting()));
        List<NivelMosaicoDTO> niveis = categorias.stream()
                .map(c -> new NivelMosaicoDTO(c.getNome(), c.getHierarquia(),
                        tomPorHierarquia.get(c.getHierarquia()), porHierarquia.getOrDefault(c.getHierarquia(), 0L)))
                .toList();

        return new MosaicoDTO(pecas, total, porQuadrado, suaPosicao, niveis);
    }

}
