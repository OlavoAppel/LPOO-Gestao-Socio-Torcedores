package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class CategoriaSocio {

    /** Hierarquia reservada à categoria "não associado" (usuário sem plano de sócio). */
    public static final int HIERARQUIA_NAO_ASSOCIADO = 0;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal mensalidade;
    private String nome;
    private BigDecimal descontoIngresso;
    private Integer hierarquia;

    @ManyToMany
    private List<SetorEstadio> setoresDiponiveis;


    public boolean isNaoAssociado() {
        return hierarquia != null && hierarquia == HIERARQUIA_NAO_ASSOCIADO;
    }

}
