package com.ifsul.lpoo.sociotorcedor.core.model.base;

import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "INGRESSO", indexes = @Index(name = "idx_ingresso_dh_compra", columnList = "ING_DATAHORACOMPRA"))
@Entity
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ING_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ING_ASSOCIADO", referencedColumnName = "ASSOCIADO_ID")
    private Associado associado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ING_LOTE", referencedColumnName = "LOT_ID")
    private LoteIngresso lote;

    @Enumerated(EnumType.STRING)
    @Column(name = "ING_STATUS")
    private StatusIngresso status;

    @Column(name = "ING_PRECOPAGO")
    private BigDecimal precoPago;      // snapshot do valor no momento da compra
    @Column(name = "ING_DATAHORACOMPRA")
    private LocalDateTime dhCompra;

    @Column(name = "ING_CODIGO", unique = true)
    private String codigo;             // UUID gerado na compra

}
