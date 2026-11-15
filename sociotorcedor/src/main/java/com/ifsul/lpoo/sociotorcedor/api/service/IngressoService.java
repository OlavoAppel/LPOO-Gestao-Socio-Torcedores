package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.stereotype.Service;

@Service
public class IngressoService {

    public void comprarIngresso(ContextoCompra contextoCompra){
        Jogo jogo = InjectionProvider.getJogoRepository().findById(contextoCompra.getJogoId()).orElseThrow();
        Associado associado = contextoCompra.getUsuario().getAssociado();




    }


}
