package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "TIME")
@Entity
public class Time {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TME_ID")
    private Long id;

    @Column(name = "TME_NOME")
    private String nome;

    @Column(name = "TME_DIVISAO")
    private Integer divisao;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TME_LOCALIDADE", referencedColumnName = "LOC_ID")
    private Localidade localidade;

}
