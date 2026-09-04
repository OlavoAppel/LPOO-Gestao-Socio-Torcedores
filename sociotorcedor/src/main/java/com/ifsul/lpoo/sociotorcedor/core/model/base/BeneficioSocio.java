package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "BENEFICIOS")
@Entity
public class BeneficioSocio {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BEN_ID")
    private Long id;

    @Column(name = "BEN_NOME")
    private String nome;

    @Column(name = "BEN_DESCRICAO")
    private String descricao;

    @Column(name = "BEN_DESCONTOINGRESSO")
    private BigDecimal descontoIngresso;

    @Column(name = "BEN_DESCONTOBRINDE")
    private BigDecimal descontoBrindes;

    @Column(name = "BEN_CATEGORIA")
    private CategoriaSocio categoria;

}
