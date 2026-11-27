package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.IngressoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.api.repository.LoteIngressoRepository;
import com.ifsul.lpoo.sociotorcedor.core.dto.LoteIngressoDTO;
import com.ifsul.lpoo.sociotorcedor.core.exception.CompraIngressoException;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Ingresso;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import com.ifsul.lpoo.sociotorcedor.core.validation.lote.LoteValidationHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoteIngressoService {

    private final LoteValidationHandle loteValidationChain;

    @Autowired
    private LoteIngressoRepository loteIngressoRepository;

    @Autowired
    private IngressoRepository ingressoRepository;

    public LoteIngressoService(
            @Qualifier("loteValidationChain")
            LoteValidationHandle loteValidationChain){
        this.loteValidationChain = loteValidationChain;
    }

    public List<LoteIngressoDTO> findLotes(ContextoCompra contextoCompra){
        Jogo jogo = InjectionProvider.getJogoRepository().findById(contextoCompra.getJogoId()).orElseThrow();
        List<LoteIngresso> loteIngressosDiponiveis = jogo.getLoteIngressoList();
        List<LoteIngressoDTO> loteDtoList = new ArrayList<>();

        for(LoteIngresso loteDisponivel : loteIngressosDiponiveis){
            try{
                ValidationResult result = loteValidationChain.handle(loteDisponivel, contextoCompra);
                LoteIngressoDTO loteDto = DTOFactory.createLoteIngressoDTO(loteDisponivel, result);
                loteDtoList.add(loteDto);
            } catch (Exception e){
                LoteIngressoDTO loteDto = DTOFactory.createLoteIngressoDTO(loteDisponivel);
                loteDtoList.add(loteDto);
            }
        }

        return loteDtoList;
    }

    @Transactional // se algo falhar, tudo é desfeito
    public void comprarIngresso(ContextoCompra contextoCompra){
        Usuario usuario = contextoCompra.getUsuario();
        if (usuario == null || usuario.getAssociado() == null) {
            throw new CompraIngressoException("É necessário estar logado como torcedor para comprar");
        }
        Associado associado = usuario.getAssociado();

        LoteIngresso lote = loteIngressoRepository.findById(contextoCompra.getLoteId())
                .orElseThrow(() -> new CompraIngressoException("Lote não encontrado"));
        ValidationResult result = loteValidationChain.handle(lote, contextoCompra);

        if(!result.isValido()){ // validação dupla no backend para evitar requisições não autorizadas (via postman etc...)
            throw new CompraIngressoException(result.getMotivo());
        }

        // Abate o estoque primeiro: se outro comprador levou o último, o UPDATE não afeta nenhuma linha.
        if (loteIngressoRepository.decrementarIngressosDisponiveis(lote.getId(), 1) == 0) {
            throw new CompraIngressoException("Ingressos esgotados para este lote");
        }

        Ingresso ingresso = new Ingresso();
        ingresso.setAssociado(associado);
        ingresso.setLote(lote);
        ingresso.setPrecoPago(associado.getCategoria().aplicarDescontoIngresso(lote.getPrecoBase()));
        ingresso.setStatus(StatusIngresso.PAGO); // pagamento simulado: a compra já nasce paga
        ingresso.setDhCompra(LocalDateTime.now());
        ingresso.setCodigo(UUID.randomUUID().toString());

        ingressoRepository.save(ingresso);
    }

}
