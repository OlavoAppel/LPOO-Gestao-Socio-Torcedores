package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.factory.UsuarioFactory;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroService {

    @Autowired
    private UsuarioFactory usuarioFactory;

    public void cadastrar(CadastroForm form){
        Usuario usuario = usuarioFactory.createNewUsuario(form);
        InjectionProvider.getUsuarioRepository().save(usuario);
    }

}
