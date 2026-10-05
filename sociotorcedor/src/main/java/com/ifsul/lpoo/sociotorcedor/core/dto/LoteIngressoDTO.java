package com.ifsul.lpoo.sociotorcedor.core.dto;

import java.math.BigDecimal;

public record LoteIngressoDTO(
    Long id,
    String nome,
    Boolean valido,
    BigDecimal precoComDesconto,
    BigDecimal precoBase,
    String motivo
) {}
