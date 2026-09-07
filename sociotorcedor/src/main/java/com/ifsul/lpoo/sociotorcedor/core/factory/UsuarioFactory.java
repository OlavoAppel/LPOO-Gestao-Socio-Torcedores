package com.ifsul.lpoo.sociotorcedor.core.factory;

import com.ifsul.lpoo.sociotorcedor.api.service.CadastroService;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.beans.factory.config.CustomEditorConfigurer;

public class UsuarioFactory {

    public static Usuario createNewUsuario(CadastroForm form){ // todas as contas iniciam sem categoria de socio
        Associado associado = new Associado();
        Usuario usuario = new Usuario();

        associado.setCategoria(CategoriaSocio.NAO_ASSOCIADO);
        associado.setNome(form.getNome());
        associado.setTelefone(form.getTelefone());
        associado.setCpf(associado.getCpf());
        associado.setDataNascimento(form.getDataNascimento());

        usuario.setEmail(form.getEmail());
        usuario.setUsername(form.getUsername());
        usuario.setPassword(form.getSenha());
        usuario.setRole("USER");

        usuario.setAssociado(associado);


        return usuario;
    }

}
