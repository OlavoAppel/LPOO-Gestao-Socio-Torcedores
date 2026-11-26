package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);

    // Uma consulta por campo (e não um "or" único): o admin não tem Associado, e o join implícito
    // com associado.cpf descartava o usuário inteiro, deixando "admin" ser cadastrado de novo.
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByAssociadoCpf(String cpf);

}
