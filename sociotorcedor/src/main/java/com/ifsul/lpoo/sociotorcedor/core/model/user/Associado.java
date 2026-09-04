package com.ifsul.lpoo.sociotorcedor.core.model.user;

import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "ASSOCIADO")
@Entity
public class Associado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASSOC_ID")
    private Long id;

    @Column(name = "ASSOC_NOME")
    private String nome;

    @Column(name = "ASSOC_CATEGORIA")
    private CategoriaSocio categoria;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSOC_USUARIO", referencedColumnName = "USER_ID")
    private Usuario usuario;

}