package com.ifsul.lpoo.sociotorcedor.config;

import com.ifsul.lpoo.sociotorcedor.core.validation.lote.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoteValidationConfig {

    /**
     * A ordem é a de checagem, e o motivo mostrado ao torcedor é o do primeiro elo que recusa:
     * primeiro o que vale para todos (janela de venda, estoque), depois o que depende de quem compra
     * (fase exclusiva, categoria, setor, limite por jogo). Um elo novo só entra na cadeia se for ligado aqui.
     */
    @Bean
    public LoteValidationHandle loteValidationChain(
            IntervaloLoteValidator intervaloValidator,
            EstoqueLoteValidator estoqueValidator,
            VendaExclusivaValidator vendaExclusivaValidator,
            CategoriaMinimaValidator categoriaMinimaValidator,
            SetorPermitidoValidator setorPermitidoValidator,
            LimitePorJogoValidator limitePorJogoValidator
    ){
        intervaloValidator
                .setProximo(estoqueValidator)
                .setProximo(vendaExclusivaValidator)
                .setProximo(categoriaMinimaValidator)
                .setProximo(setorPermitidoValidator)
                .setProximo(limitePorJogoValidator);
        return intervaloValidator;
    }
}
