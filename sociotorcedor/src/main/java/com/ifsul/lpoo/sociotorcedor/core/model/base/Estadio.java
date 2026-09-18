package com.ifsul.lpoo.sociotorcedor.core.model.base;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "ESTADIO")
@Entity
public class Estadio {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EST_ID")
    private Long id;

    @Column(name = "EST_NOME")
    private String nome;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "EST_ENDERECO", referencedColumnName = "END_ID")
//    private Endereco endereco;

    @OneToMany(mappedBy = "estadio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetorEstadio> setores;

}
