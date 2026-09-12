package com.ifsul.lpoo.sociotorcedor.core.model.base;

import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
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

    @Id @GeneratedValue
    private Long id;

    private BigDecimal mensalidade;
    private String nome;
    private BigDecimal descontoIngresso;
    private Integer hierarquia;

    @ManyToMany
    private List<SetorEstadio> setoresDiponiveis;

}
