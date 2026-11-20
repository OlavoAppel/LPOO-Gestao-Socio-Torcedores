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

@Table(name = "LOTE_INGRESSO")
@Entity
public class LoteIngresso {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LOT_JOGO", referencedColumnName = "JOG_ID")
    private Jogo jogo;

    @ManyToOne
    @JoinColumn(name = "LOT_SETOR", referencedColumnName = "SET_ID")
    private SetorEstadio setor;

    @Column(name = "LOT_NOME")
    private String nome;

    @Column(name = "LOT_PRECOBASE")
    private BigDecimal precoBase;
    @Column(name = "LOT_INGRESSOSTOTAIS")
    private Integer ingressosTotaisLote;
    @Column(name = "LOT_INGRESSOSDISPONIVEIS")
    private Integer ingressoDisponiveis;
    @Column(name = "LOT_DHINICIOVENDA")
    private LocalDateTime dhInicioVenda;
    @Column(name = "LOT_DHTERMINOVENDAEXCLUSIVA")
    private LocalDateTime dhTerminoVendaExclusiva;

    @ManyToOne
    @JoinColumn(name = "LOT_CATEGORIAMINIMA", referencedColumnName = "CAT_ID")
    private CategoriaSocio categoriaMinima;

}
