package com.ifsul.lpoo.sociotorcedor.api.service;

import com.ifsul.lpoo.sociotorcedor.api.repository.InjectionProvider;
import com.ifsul.lpoo.sociotorcedor.core.dto.LoteIngressoDTO;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;
import com.ifsul.lpoo.sociotorcedor.core.validation.lote.LoteValidationHandle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoteService {

    private final LoteValidationHandle loteValidationChain;

    public LoteService(
            @Qualifier("loteValidationChain")
            LoteValidationHandle loteValidationChain){
        this.loteValidationChain = loteValidationChain;
    }

    public List<LoteIngressoDTO> findLotes(ContextoCompra contextoCompra, Long jogoId){
        Jogo jogo = InjectionProvider.getJogoRepository().findById(jogoId).orElseThrow();
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


}
