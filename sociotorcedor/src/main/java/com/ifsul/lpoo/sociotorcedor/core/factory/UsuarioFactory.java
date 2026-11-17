package com.ifsul.lpoo.sociotorcedor.core.factory;

import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.api.repository.CategoriaSocioRepository;
import com.ifsul.lpoo.sociotorcedor.core.exception.SocioTorcedorException;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsuarioFactory {

    private final PasswordEncoder passwordEncoder;
    private final CategoriaSocioRepository categoriaSocioRepository;

    public Usuario createNewUsuario(CadastroForm form) {
        Associado associado = new Associado();
        Usuario usuario = new Usuario();

        CategoriaSocio naoAssociado = categoriaSocioRepository
                .findByHierarquia(CategoriaSocio.HIERARQUIA_NAO_ASSOCIADO)
                .orElseThrow(() -> new SocioTorcedorException("Categoria inicial (não associado) não está cadastrada"));

        associado.setCategoria(naoAssociado);
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
