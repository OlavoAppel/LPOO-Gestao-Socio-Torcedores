package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "LOCALIDADE")
@Entity
public class Localidade {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOC_ID")
    private Long id;

    @Column(name = "LOC_CIDADE")
    private String cidade;

    @Column(name = "LOC_ESTADO")
    private String estado;

    @Column(name = "LOC_UF")
    private String uf;

}
