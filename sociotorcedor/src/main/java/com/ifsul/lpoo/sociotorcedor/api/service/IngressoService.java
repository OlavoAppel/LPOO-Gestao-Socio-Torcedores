package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.stereotype.Service;

@Service
public class IngressoService {

    public void comprarIngresso(Long jogoId, Usuario usuario){
        Jogo jogo = InjectionProvider.getJogoRepository().findById(jogoId).orElseThrow();
        Associado associado = usuario.getAssociado();



    }


}
