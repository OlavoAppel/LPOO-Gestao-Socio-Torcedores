package com.ifsul.lpoo.sociotorcedor.api.repository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InjectionProvider {

    @Autowired
    private UsuarioRepository usuarioRepositoryInject;

    @Getter private static UsuarioRepository usuarioRepository;

    @PostConstruct
    public void init(){
        InjectionProvider.usuarioRepository = usuarioRepositoryInject;
    }
}
