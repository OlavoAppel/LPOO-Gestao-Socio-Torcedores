package com.ifsul.lpoo.sociotorcedor.migration;

import com.ifsul.lpoo.sociotorcedor.api.repository.UsuarioRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@sociotorcedor.com");
            admin.setRole("ADMIN");
            usuarioRepository.save(admin);
        }
    }
}
