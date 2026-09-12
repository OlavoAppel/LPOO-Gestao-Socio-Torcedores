package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class IntervaloLoteValidator extends LoteValidationHandle {

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto){
        LocalDateTime dhInicio = lote.getDhInicioVenda(), dhFim = lote.getJogo().getDhJogo();
        LocalDateTime dhLocal = contexto.getDhRequisicao();

        if(dhLocal.isBefore(dhInicio)){
            return new ValidationResult(String.format("Início da venda do lotem em %s", dhInicio.toString()));
        }

        if(dhLocal.isAfter(dhFim)){
            return new ValidationResult("Venda do lote fechada, jogo já aconteceu");
        }

        return new ValidationResult("");
    }

}
