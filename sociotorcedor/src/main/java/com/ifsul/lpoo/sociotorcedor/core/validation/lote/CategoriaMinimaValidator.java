package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.stereotype.Component;

/** Regra 4: se o lote define uma categoria mínima, o comprador precisa ter hierarquia igual ou maior. */
@Component
public class CategoriaMinimaValidator extends LoteValidationHandle {

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto) {
        CategoriaSocio minima = lote.getCategoriaMinima();
        Associado comprador = compradorDe(contexto);

        if (minima == null || comprador == null) {
            return aprovado();
        }
        if (comprador.getCategoria().getHierarquia() < minima.getHierarquia()) {
            return recusado("Este lote é para sócios " + minima.getNome() + " ou de categoria superior");
        }
        return aprovado();
    }
}
