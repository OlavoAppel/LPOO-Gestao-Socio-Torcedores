package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.form.AssociarForm;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.stereotype.Service;

@Service
public class SocioTorcedorService {

    public void cadastrarSocio(AssociarForm form, Usuario usuario){
        usuario.getAssociado().setCategoria(form.getCategoriaSocio());
        InjectionProvider.getUsuarioRepository().save(usuario);
    }

}
