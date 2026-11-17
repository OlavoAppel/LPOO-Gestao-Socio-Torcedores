package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
