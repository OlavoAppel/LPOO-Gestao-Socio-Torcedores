package com.ifsul.lpoo.sociotorcedor.core.factory;

import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsuarioFactory {

    private final PasswordEncoder passwordEncoder;

    public Usuario createNewUsuario(CadastroForm form) {
        Associado associado = new Associado();
        Usuario usuario = new Usuario();

        associado.setCategoria(Categoria.NAO_ASSOCIADO);
        associado.setNome(form.getNome());
        associado.setTelefone(form.getTelefone());
        associado.setCpf(form.getCpf());
        associado.setDataNascimento(form.getDataNascimento());

        usuario.setEmail(form.getEmail());
        usuario.setUsername(form.getUsername());
        usuario.setPassword(passwordEncoder.encode(form.getSenha()));
        usuario.setRole("USER");

        usuario.setAssociado(associado);

        return usuario;
    }
}
