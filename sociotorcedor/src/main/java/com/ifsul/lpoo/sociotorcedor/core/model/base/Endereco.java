package com.ifsul.lpoo.sociotorcedor.core.model.base;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "ENDERECO")
@Entity
public class Endereco {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "END_ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "END_LOCALIDADE", referencedColumnName = "LOC_ID")
    private Localidade localidade;

    @Column(name = "END_CEP")
    private String cep;

    @Column(name = "END_RUA")
    private String rua;

    @Column(name = "END_NUMERO")
    private Integer numero;

}
