package com.ifsul.lpoo.sociotorcedor.core.model.base;

import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class LoteIngresso {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Jogo jogo;

    @ManyToOne
    private SetorEstadio setor;

    private String nome;

    private BigDecimal precoBase;
    private Integer ingressoDisponiveis;
    private LocalDateTime dhInicioVenda;
    private LocalDateTime dhTerminoVendaExclusiva;

    @ManyToOne
    private CategoriaSocio categoriaMinima;

}
