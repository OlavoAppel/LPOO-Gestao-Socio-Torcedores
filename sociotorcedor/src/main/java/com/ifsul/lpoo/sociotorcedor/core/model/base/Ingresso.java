package com.ifsul.lpoo.sociotorcedor.core.model.base;

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

@Entity
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "associado_id")
    private Associado associado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id")
    private LoteIngresso lote;

    //@Enumerated(EnumType.STRING)
    private String status; // RESERVADO, PAGO, EXPIRADO, CANCELADO

    private BigDecimal precoPago;      // snapshot do valor no momento da compra
    private LocalDateTime dhCompra;

    @Column(unique = true)
    private String codigo;             // UUID, é o que vai no "QR code" da entrada

}
