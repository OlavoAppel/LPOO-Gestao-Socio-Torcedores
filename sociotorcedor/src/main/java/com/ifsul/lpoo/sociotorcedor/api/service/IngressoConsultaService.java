package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.IngressoRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Ingresso;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** Consultas de ingressos para as telas (a compra continua em {@link LoteIngressoService}). */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngressoConsultaService {

    private final IngressoRepository ingressoRepository;

    /** O ingresso comprado por último pelo usuário (mostrado na confirmação da compra). */
    public Optional<Ingresso> ultimoDoUsuario(String username) {
        return ingressoRepository.buscarDoUsuarioPorCompraRecente(username, Limit.of(1)).stream().findFirst();
    }

}
