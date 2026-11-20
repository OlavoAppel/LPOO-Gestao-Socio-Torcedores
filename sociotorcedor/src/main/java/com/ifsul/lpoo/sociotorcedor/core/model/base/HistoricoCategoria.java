package com.ifsul.lpoo.sociotorcedor.core.model.base;

import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "HISTORICO_CATEGORIA", indexes = @Index(name = "idx_historico_categoria_data", columnList = "HIST_DATA"))
@Entity
public class HistoricoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HIST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HIST_ASSOCIADO", referencedColumnName = "ASSOCIADO_ID")
    private Associado associado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HIST_CATEGORIAANTERIOR", referencedColumnName = "CAT_ID")
    private CategoriaSocio categoriaAnterior;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HIST_CATEGORIANOVA", referencedColumnName = "CAT_ID")
    private CategoriaSocio categoriaNova;

    @Column(name = "HIST_DATA")
    private LocalDate data;

}
