package com.ifsul.lpoo.sociotorcedor.api.repository;

import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    List<Jogo> findByDhJogoBetween(LocalDateTime dhInicaio, LocalDateTime dhFim);

}


