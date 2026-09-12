package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InjectionProvider {

    @Autowired
    private UsuarioRepository usuarioRepositoryInject;

    @Autowired
    private AssociadoRepository associadoRepositoryInject;

    @Autowired
    private JogoRepository jogoRepositoryInject;

    @Getter private static UsuarioRepository usuarioRepository;
    @Getter private static AssociadoRepository associadoRepository;
    @Getter private static JogoRepository jogoRepository;

    @PostConstruct
    public void init(){
        InjectionProvider.usuarioRepository = usuarioRepositoryInject;
        InjectionProvider.associadoRepository = associadoRepositoryInject;
        InjectionProvider.jogoRepository = jogoRepositoryInject;
    }
}
