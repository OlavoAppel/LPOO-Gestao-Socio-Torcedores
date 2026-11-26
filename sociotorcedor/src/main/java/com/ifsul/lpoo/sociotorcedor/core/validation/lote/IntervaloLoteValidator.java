package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/** Regra 1: o pedido só vale entre o início da venda do lote e o horário do jogo. */
@Component
public class IntervaloLoteValidator extends LoteValidationHandle {

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto){
        LocalDateTime dhInicio = lote.getDhInicioVenda(), dhFim = lote.getJogo().getDhJogo();
        LocalDateTime dhLocal = contexto.getDhRequisicao();

        if(dhInicio == null){
            return recusado("A venda deste lote ainda não tem data de início");
        }

        if(dhLocal.isBefore(dhInicio)){
            return recusado("Início da venda do lote em " + formatar(dhInicio));
        }

        if(dhLocal.isAfter(dhFim)){
            return recusado("Venda do lote fechada, jogo já aconteceu");
        }

        return aprovado();
    }

}
