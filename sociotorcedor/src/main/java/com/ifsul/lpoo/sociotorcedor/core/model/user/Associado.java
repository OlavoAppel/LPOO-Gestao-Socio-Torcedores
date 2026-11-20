package com.ifsul.lpoo.sociotorcedor.core.model.user;

import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "ASSOCIADOS", indexes = @Index(name = "idx_associado_data_adesao", columnList = "ASSOCIADO_DATAADESAO"))
@Entity
public class Associado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASSOCIADO_ID")
    private Long id;

    @Column(name = "ASSOCIADO_NOME")
    private String nome;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ASSOCIADO_CATEGORIA")
    private CategoriaSocio categoria;

    /** Data em que virou sócio; nula enquanto for "não associado". */
    @Column(name = "ASSOCIADO_DATAADESAO")
    private LocalDate dataAdesao;

    @Column(name = "ASSOCIADO_CPF", length = 11)
    private String cpf;

    @Column(name = "ASSOCIADO_TELEFONE")
    private String telefone;

    @Column(name = "ASSOCIADO_DATANASCIMENTO")
    private LocalDate dataNascimento;

    /** Lado inverso: a chave está em Usuario.USER_ASSOCIADO, então não há coluna aqui. */
    @OneToOne(mappedBy = "associado", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

}