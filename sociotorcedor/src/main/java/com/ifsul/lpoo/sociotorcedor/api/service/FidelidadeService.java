package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.core.dto.PontosDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;

/**
 * Programa de pontos do sócio. Os controllers dependem só desta interface; hoje a implementação é
 * {@code mock.FidelidadeMockService} (prévia com dados de demonstração). Ver docs/front/dados-ficticios.md.
 */
public interface FidelidadeService {

    /** Saldo, extrato e catálogo de recompensas do sócio. */
    PontosDTO resumoDo(Usuario usuario);

}
