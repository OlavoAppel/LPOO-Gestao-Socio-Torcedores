package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.stereotype.Component;

/**
 * Regra 2: o lote precisa ter ingresso disponível. A compra ainda abate o estoque com um UPDATE atômico
 * (corrida entre dois compradores); esta regra serve para a tela já mostrar o lote como esgotado.
 */
@Component
public class EstoqueLoteValidator extends LoteValidationHandle {

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto) {
        Integer disponiveis = lote.getIngressoDisponiveis();
        if (disponiveis == null || disponiveis <= 0) {
            return recusado("Ingressos esgotados para este lote");
        }
        return aprovado();
    }
}
