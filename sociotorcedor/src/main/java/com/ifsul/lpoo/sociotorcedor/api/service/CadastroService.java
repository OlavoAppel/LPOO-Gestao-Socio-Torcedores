package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.UsuarioRepository;
import com.ifsul.lpoo.sociotorcedor.core.exception.CadastroException;
import com.ifsul.lpoo.sociotorcedor.core.factory.UsuarioFactory;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroService {

    private final UsuarioFactory usuarioFactory;
    private final UsuarioRepository usuarioRepository;

    public void cadastrar(CadastroForm form) {
        // Cada dado único é conferido separadamente para a tela apontar o campo certo.
        if (usuarioRepository.existsByUsernameIgnoreCase(form.getUsername())) {
            throw new CadastroException("username", "Esse usuário já está em uso. Escolha outro.");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(form.getEmail())) {
            throw new CadastroException("email", "Já existe uma conta com esse e-mail.");
        }
        if (usuarioRepository.existsByAssociadoCpf(form.getCpf())) {
            throw new CadastroException("cpf", "Já existe uma conta com esse CPF.");
        }

        Usuario usuario = usuarioFactory.createNewUsuario(form);
        usuarioRepository.save(usuario);
    }

}
