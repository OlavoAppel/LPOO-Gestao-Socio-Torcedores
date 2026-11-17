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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "associado_id")
    private Associado associado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoriaAnterior_id")
    private CategoriaSocio categoriaAnterior;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoriaNova_id")
    private CategoriaSocio categoriaNova;

    @Column(name = "HIST_DATA")
    private LocalDate data;

}
