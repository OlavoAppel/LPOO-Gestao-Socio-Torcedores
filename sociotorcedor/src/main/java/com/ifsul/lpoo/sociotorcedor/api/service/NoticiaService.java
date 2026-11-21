package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.core.dto.NoticiaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Notícias do clube. Os controllers dependem só desta interface; hoje a implementação é
 * {@code mock.NoticiaMockService} (dados fixos em memória). Uma implementação real com banco
 * pode substituí-la sem mudar controller nem template (ver docs/front/dados-ficticios.md).
 */
public interface NoticiaService {

    /** Todas as notícias, da mais recente para a mais antiga. */
    List<NoticiaDTO> listar();

    /** As mais recentes, para a página inicial. */
    List<NoticiaDTO> recentes(int quantidade);

    Optional<NoticiaDTO> buscar(Long id);

}
