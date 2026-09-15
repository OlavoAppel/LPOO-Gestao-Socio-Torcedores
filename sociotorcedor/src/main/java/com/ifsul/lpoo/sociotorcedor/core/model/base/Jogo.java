package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionIdJdbcTypeCode;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "JOGO")
@Entity
public class Jogo {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "JOG_ID")
    private Long id;

    @Column(name = "JOG_DATAHORA")
    private LocalDateTime dhJogo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "JOG_ESTADIO", referencedColumnName = "EST_ID")
    private Estadio estadio;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "JOG_TIMECASA", referencedColumnName = "TME_ID")
    private Time casa;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "JOG_TIMEFORA", referencedColumnName = "TME_ID")
    private Time fora;

    private String campeonato;

    @OneToMany
    private List<LoteIngresso> loteIngressoList;

}
