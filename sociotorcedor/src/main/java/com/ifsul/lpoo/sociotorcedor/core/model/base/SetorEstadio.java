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

@Table(name = "SETOR_ESTADIO")
@Entity
public class SetorEstadio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SET_ID")
    private Long id;

    @Column(name = "SET_CAPACIDADETOTAL")
    private Integer capacidadeTotal;
    @Column(name = "SET_NOME")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "SET_ESTADIO", referencedColumnName = "EST_ID")
    private Estadio estadio;

    @ManyToMany
    @JoinTable(name = "SETOR_CATEGORIA_PERMITIDA",
            joinColumns = @JoinColumn(name = "SCP_SETOR", referencedColumnName = "SET_ID"),
            inverseJoinColumns = @JoinColumn(name = "SCP_CATEGORIA", referencedColumnName = "CAT_ID"))
    private List<CategoriaSocio> sociosPermitidos;

}
