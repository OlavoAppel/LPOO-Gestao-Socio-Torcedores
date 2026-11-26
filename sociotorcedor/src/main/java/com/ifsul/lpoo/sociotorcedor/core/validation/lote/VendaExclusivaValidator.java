package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Regra 3: enquanto a venda exclusiva (até {@code dhTerminoVendaExclusiva}) não termina, só sócio compra.
 * Quem ainda é "não associado" espera o fim da fase exclusiva ou se associa. Sem a data, o lote já abre para todos.
 */
@Component
public class VendaExclusivaValidator extends LoteValidationHandle {

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto) {
        LocalDateTime termino = lote.getDhTerminoVendaExclusiva();
        Associado comprador = compradorDe(contexto);

        if (termino == null || comprador == null || !contexto.getDhRequisicao().isBefore(termino)) {
            return aprovado();
        }
        if (comprador.getCategoria().isNaoAssociado()) {
            return recusado("Venda exclusiva para sócios até " + formatar(termino)
                    + ". Associe-se para comprar agora ou volte depois desse horário");
        }
        return aprovado();
    }
}
