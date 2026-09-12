package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import jakarta.validation.Valid;

import java.util.List;

public abstract class LoteValidationHandle {

    private LoteValidationHandle proximo;

    public LoteValidationHandle setProximo(LoteValidationHandle proximo){
        this.proximo = proximo;
        return proximo;
    }

    public final ValidationResult handle(LoteIngresso lote, ContextoCompra contexto){
        ValidationResult result = validar(lote, contexto);

        if(!result.isValido() || proximo == null){
            return result;
        }

        return proximo.handle(lote, contexto);
    }

    protected abstract ValidationResult validar(LoteIngresso transporte, ContextoCompra contexto);
}
