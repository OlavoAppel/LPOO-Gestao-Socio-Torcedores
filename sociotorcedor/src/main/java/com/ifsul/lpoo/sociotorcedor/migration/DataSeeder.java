package com.ifsul.lpoo.sociotorcedor.migration;

import com.ifsul.lpoo.sociotorcedor.api.repository.EstadioRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.TimeRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.UsuarioRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Estadio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Time;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JogoRepository jogoRepository;
    private final TimeRepository timeRepository;
    private final EstadioRepository estadioRepository;

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

        if (jogoRepository.count() > 0) {
            return; // Evita duplicar dados a cada reinício
        }

        Time saoPaulo = criarTime("São Paulo FC", 1, "SPFC", "/images/escudos/spfc.png");
        Time atleticoMineiro = criarTime("Atlético Mineiro", 1, "CAM", "/images/escudos/cam.png");
        Time bocaJuniors = criarTime("Boca Juniors", 1, "CABJ", "/images/escudos/cabj.png");
        Time inter = criarTime("Internacional", 1, "INT", "/images/escudos/int.png");
        Time flamengo = criarTime("Flamengo", 1, "FLA", "/images/escudos/fla.png");
        Time gremio = criarTime("Grêmio", 1, "GRE", "/images/escudos/gre.png");

        Estadio morumBIS = criarEstadio("MorumBIS");
        Estadio laBombonera = criarEstadio("La Bombonera");

        timeRepository.saveAll(List.of(saoPaulo, atleticoMineiro, bocaJuniors, inter, flamengo, gremio));
        estadioRepository.saveAll(List.of(morumBIS, laBombonera));

        LocalDateTime agora = LocalDateTime.now();

        // 1. Jogo Passado (Setembro)
        Jogo jogoAnterior1 = criarJogo(
                agora.minusDays(11).withHour(18).withMinute(30),
                "Brasileirão 2026",
                morumBIS,
                saoPaulo,
                gremio
        );

        // 2. Jogo Passado Recente (Setembro)
        Jogo jogoAnterior2 = criarJogo(
                agora.minusDays(8).withHour(21).withMinute(30),
                "CONMEBOL Sul-Americana 2026",
                laBombonera
                ,gremio,
                flamengo
        );

        Jogo jogoAnterior3 = criarJogo(
                agora.minusMonths(1).withHour(21).withMinute(30),
                "Brasileirão 2026",
                morumBIS,
                atleticoMineiro,
                gremio
        );

        Jogo jogoAnterior4 = criarJogo(
                agora.minusMonths(2).withHour(21).withMinute(30),
                "Brasileirão 2026",
                morumBIS
                ,inter,
                gremio
        );

        // 3. PRÓXIMO JOGO (Próximo evento no futuro)
        Jogo proximoJogo = criarJogo(
                agora.plusDays(3).withHour(21).withMinute(00),
                "Brasileirão 2026",
                morumBIS,
                saoPaulo,
                gremio

        );

        // 4. Jogo Futuro (Outubro)
        Jogo jogoFuturo = criarJogo(
                agora.plusDays(20).withHour(16).withMinute(00),
                "Brasileirão 2026",
                morumBIS,
                gremio,
                atleticoMineiro
        );

        // Salva todos os jogos (CascadeType.ALL vai salvar os Estádios e Times automaticamente)
        jogoRepository.saveAll(List.of(jogoAnterior1, jogoAnterior2, jogoAnterior3, jogoAnterior4, proximoJogo, jogoFuturo));
    }

    private Time criarTime(String nome, Integer divisao, String sigla, String escudoPath) {
        Time time = new Time();
        time.setNome(nome);
        time.setDivisao(divisao);
        time.setSigla(sigla);
        time.setEscudoPath(escudoPath);
        return time;
    }

    private Estadio criarEstadio(String nome) {
        Estadio estadio = new Estadio();
        estadio.setNome(nome);
        return estadio;
    }

    private Jogo criarJogo(LocalDateTime dhJogo, String campeonato, Estadio estadio, Time casa, Time fora) {
        Jogo jogo = new Jogo();
        jogo.setDhJogo(dhJogo);
        jogo.setCampeonato(campeonato);
        jogo.setEstadio(estadio);
        jogo.setCasa(casa);
        jogo.setFora(fora);
        return jogo;
    }
}
