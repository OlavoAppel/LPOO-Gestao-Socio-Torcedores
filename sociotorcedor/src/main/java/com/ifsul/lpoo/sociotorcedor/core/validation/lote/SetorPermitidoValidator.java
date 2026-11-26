package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.base.SetorEstadio;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Regra 5: o setor do estádio pode limitar as categorias que entram nele (ex.: camarote só Prata e Ouro).
 * Lista vazia significa sem restrição.
 */
@Component
public class SetorPermitidoValidator extends LoteValidationHandle {

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto) {
        SetorEstadio setor = lote.getSetor();
        Associado comprador = compradorDe(contexto);
        if (setor == null || comprador == null) {
            return aprovado();
        }

        List<CategoriaSocio> permitidas = setor.getSociosPermitidos();
        if (permitidas == null || permitidas.isEmpty()) {
            return aprovado();
        }

        boolean entra = permitidas.stream()
                .anyMatch(c -> Objects.equals(c.getId(), comprador.getCategoria().getId()));
        if (!entra) {
            String nomes = permitidas.stream().map(CategoriaSocio::getNome).collect(Collectors.joining(", "));
            return recusado("O setor " + setor.getNome() + " é só para as categorias: " + nomes);
        }
        return aprovado();
    }
}
