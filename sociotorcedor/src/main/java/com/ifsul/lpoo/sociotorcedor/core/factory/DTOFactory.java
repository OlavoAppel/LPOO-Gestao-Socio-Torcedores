package com.ifsul.lpoo.sociotorcedor.core.factory;

import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.LoteIngressoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.TimeDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;

public class DTOFactory {

    public static LoteIngressoDTO createLoteIngressoDTO(LoteIngresso lote, ValidationResult result){
        return new LoteIngressoDTO(
                lote.getId(),
                lote.getSetor().getNome(),
                result.isValido(),
                result.getMotivo()
        );
    }

    public static LoteIngressoDTO createLoteIngressoDTO(LoteIngresso lote){
        return new LoteIngressoDTO(
                lote.getId(),
                lote.getSetor().getNome(),
                false,
                "Erro ao consultar lote."
        );
    }

    public static JogoDTO createJogoDTO(Jogo jogo){
        JogoDTO jogoDTO = JogoDTO.builder()
                .dhJogo(jogo.getDhJogo())
                .nomeEstadio(jogo.getEstadio().getNome())
                .campeonato(jogo.getCampeonato())
                .build();

        TimeDTO timeCasa = TimeDTO.builder()
                .sigla(jogo.getCasa().getSigla())
                .escudoPath(jogo.getCasa().getEscudoPath())
                .build();

        TimeDTO timeFora = TimeDTO.builder()
                .sigla(jogo.getFora().getSigla())
                .escudoPath(jogo.getFora().getEscudoPath())
                .build();

        jogoDTO.setCasa(timeCasa);
        jogoDTO.setFora(timeFora);

        return jogoDTO;
    }

}
