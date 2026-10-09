package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("select case when count(user) > 0 then true else false end " +
            "from Usuario user " +
            "where user.associado.cpf = :cpf " +
            "or user.username = :username " +
            "or user.email = :email")
    boolean checkDuplicateUser(
            @Param("cpf") String cpf,
            @Param("username") String username,
            @Param("email") String email
    );

}
