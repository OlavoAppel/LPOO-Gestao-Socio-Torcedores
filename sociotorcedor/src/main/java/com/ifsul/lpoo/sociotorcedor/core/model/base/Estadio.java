package com.ifsul.lpoo.sociotorcedor.core.model.base;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "ESTADIO")
@Entity
public class Estadio {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EST_NOME")
    private String nome;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "EST_ENDERECO", referencedColumnName = "END_ID")
    private Endereco endereco;

}
