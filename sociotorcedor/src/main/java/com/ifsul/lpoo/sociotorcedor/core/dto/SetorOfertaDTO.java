package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Um lote de ingressos visto pelo torcedor: setor, preços por categoria, estoque e se pode ser comprado.
 * A validade (emVenda/motivo) vem da mesma cadeia de validação usada na compra.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetorOfertaDTO {

    private Long loteId;
    /** Nome do setor do estádio (ou do lote, se o lote não tiver setor). */
    private String setor;
    private BigDecimal precoBase;
    /** Um preço por categoria, na ordem da hierarquia (o primeiro é o de quem não é sócio). */
    private List<PrecoCategoriaDTO> precos;
    /** Preço com o desconto da categoria do torcedor logado; nulo para visitante e admin. */
    private BigDecimal seuPreco;
    private Integer disponiveis;
    private Integer total;
    private boolean emVenda;
    /** Motivo informado pela validação quando o lote não está à venda. */
    private String motivo;
    /** Início da venda, quando ainda está no futuro. */
    private LocalDateTime vendaAbreEm;

    public boolean isEsgotado() {
        return disponiveis != null && disponiveis <= 0;
    }

    public boolean isComDesconto() {
        return seuPreco != null && precoBase != null && seuPreco.compareTo(precoBase) < 0;
    }

    /** Medidor de 10 quadrados: cada um vale 10% do lote. Um ingresso restante já acende um quadrado. */
    public int getQuadradosDisponiveis() {
        if (total == null || total <= 0 || disponiveis == null || disponiveis <= 0) {
            return 0;
        }
        return (int) Math.min(10, Math.ceil(disponiveis * 10.0 / total));
    }

}
