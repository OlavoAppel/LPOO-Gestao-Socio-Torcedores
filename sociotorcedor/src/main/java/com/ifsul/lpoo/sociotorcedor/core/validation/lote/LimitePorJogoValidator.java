package com.ifsul.lpoo.sociotorcedor.core.validation.lote;

import com.ifsul.lpoo.sociotorcedor.api.repository.IngressoRepository;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Regra 6: cada conta compra no máximo N ingressos por jogo (padrão 1, em {@code ingresso.limite-por-jogo}),
 * somando todos os setores. Contam os ingressos reservados e pagos; cancelados e expirados liberam a vaga.
 * Evita que uma conta esvazie um lote (cambismo).
 */
@Component
public class LimitePorJogoValidator extends LoteValidationHandle {

    private static final List<StatusIngresso> STATUS_QUE_CONTAM = List.of(StatusIngresso.RESERVADO, StatusIngresso.PAGO);

    private final IngressoRepository ingressoRepository;
    private final int limite;

    public LimitePorJogoValidator(IngressoRepository ingressoRepository,
                                  @Value("${ingresso.limite-por-jogo:1}") int limite) {
        this.ingressoRepository = ingressoRepository;
        this.limite = limite;
    }

    @Override
    protected ValidationResult validar(LoteIngresso lote, ContextoCompra contexto) {
        Associado comprador = compradorDe(contexto);
        if (comprador == null) {
            return aprovado();
        }

        long jaComprados = ingressoRepository.countByAssociado_IdAndLote_Jogo_IdAndStatusIn(
                comprador.getId(), lote.getJogo().getId(), STATUS_QUE_CONTAM);
        if (jaComprados >= limite) {
            return recusado(limite == 1
                    ? "Você já tem ingresso para este jogo. O limite é de 1 ingresso por conta"
                    : "Limite de " + limite + " ingressos por jogo atingido");
        }
        return aprovado();
    }
}
