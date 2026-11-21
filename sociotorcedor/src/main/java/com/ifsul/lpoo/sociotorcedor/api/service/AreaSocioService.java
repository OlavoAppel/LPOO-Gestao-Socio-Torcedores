package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.HistoricoCategoriaRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.CarteirinhaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.HistoricoCategoriaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.MosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Dados de leitura da área do sócio ("Minha associação"): carteirinha e histórico de categorias.
 * A troca de categoria continua só em {@link SocioTorcedorService}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaSocioService {

    private final HistoricoCategoriaRepository historicoCategoriaRepository;

    public List<HistoricoCategoriaDTO> historico(Long associadoId) {
        return historicoCategoriaRepository.buscarDoAssociado(associadoId);
    }

    public CarteirinhaDTO carteirinha(Associado associado, MosaicoDTO mosaico) {
        return DTOFactory.createCarteirinhaDTO(associado, mosaico);
    }

}
