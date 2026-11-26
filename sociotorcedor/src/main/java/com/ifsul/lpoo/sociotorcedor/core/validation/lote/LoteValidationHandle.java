package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class LoteValidationHandle {

    private static final DateTimeFormatter FORMATO_DH = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

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

    /** Resultado de "regra satisfeita": a cadeia segue para o próximo elo. */
    protected static ValidationResult aprovado() {
        return new ValidationResult("");
    }

    protected static ValidationResult recusado(String motivo) {
        return new ValidationResult(motivo);
    }

    /**
     * Quem está comprando, ou null quando é um visitante só consultando os lotes (sem conta de torcedor).
     * As regras que dependem do comprador usam isto para não recusar a consulta: a compra em si já exige login.
     */
    protected static Associado compradorDe(ContextoCompra contexto) {
        return contexto.getUsuario() == null ? null : contexto.getUsuario().getAssociado();
    }

    protected static String formatar(LocalDateTime dh) {
        return FORMATO_DH.format(dh);
    }
}
