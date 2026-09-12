package com.ifsul.lpoo.sociotorcedor.core.model.base;

import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.SetorArquibancada;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class SetorEstadio {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer capacidadeTotal;
    private String nome;

    @ManyToOne
    private Estadio estadio;

    @ManyToMany
    private List<CategoriaSocio> sociosPermitidos;

}
