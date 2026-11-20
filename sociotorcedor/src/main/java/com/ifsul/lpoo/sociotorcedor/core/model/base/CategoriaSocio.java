package com.ifsul.lpoo.sociotorcedor.core.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "CATEGORIA_SOCIO")
@Entity
public class CategoriaSocio {

    /** Hierarquia reservada à categoria "não associado" (usuário sem plano de sócio). */
    public static final int HIERARQUIA_NAO_ASSOCIADO = 0;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAT_ID")
    private Long id;

    @Column(name = "CAT_MENSALIDADE")
    private BigDecimal mensalidade;
    @Column(name = "CAT_NOME")
    private String nome;
    /** Desconto em porcentagem sobre o preço do ingresso (ex.: 10 = 10%). */
    @Column(name = "CAT_DESCONTOINGRESSO")
    private BigDecimal descontoIngresso;
    @Column(name = "CAT_HIERARQUIA")
    private Integer hierarquia;

    // lado inverso de SetorEstadio.sociosPermitidos: uma única tabela de ligação para a relação
    @ManyToMany(mappedBy = "sociosPermitidos")
    private List<SetorEstadio> setoresDiponiveis;


    /** Aplica o desconto de ingresso da categoria ao preço informado, com 2 casas decimais. */
    public BigDecimal aplicarDescontoIngresso(BigDecimal preco) {
        if (descontoIngresso == null || descontoIngresso.signum() <= 0) {
            return preco.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal fator = BigDecimal.ONE.subtract(descontoIngresso.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return preco.multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isNaoAssociado() {
        return hierarquia != null && hierarquia == HIERARQUIA_NAO_ASSOCIADO;
    }

}
