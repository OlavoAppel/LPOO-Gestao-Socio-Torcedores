package com.ifsul.lpoo.sociotorcedor.core.factory;

import com.ifsul.lpoo.sociotorcedor.core.dto.BeneficioSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.CategoriaSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.LoteIngressoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.TimeDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.base.BeneficioSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;

import java.util.List;

public class DTOFactory {

    public static BeneficioSocioDTO createBeneficioSocioDTO(BeneficioSocio beneficio){
        return new BeneficioSocioDTO(
                beneficio.getNome(),
                beneficio.getDescricao(),
                beneficio.getDescontoIngresso(),
                beneficio.getDescontoBrindes()
        );
    }

    public static CategoriaSocioDTO createCategoriaSocioDTO(CategoriaSocio categoria, List<BeneficioSocio> beneficios){
        return new CategoriaSocioDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getMensalidade(),
                categoria.getDescontoIngresso(),
                categoria.getHierarquia(),
                beneficios.stream().map(DTOFactory::createBeneficioSocioDTO).toList()
        );
    }

    public static LoteIngressoDTO createLoteIngressoDTO(LoteIngresso lote, ValidationResult result){
        return new LoteIngressoDTO(
                lote.getId(),
                lote.getNome(),
                result.isValido(),
                lote.getPrecoBase(),
                lote.getPrecoBase(),
                result.getMotivo()
        );
    }

    public static LoteIngressoDTO createLoteIngressoDTO(LoteIngresso lote){
        return new LoteIngressoDTO(
                lote.getId(),
                lote.getNome(),
                false,
                lote.getPrecoBase(),
                lote.getPrecoBase(),
                "Erro ao consultar lote."
        );
    }

    public static JogoDTO createJogoDTO(Jogo jogo){
        JogoDTO jogoDTO = JogoDTO.builder()
                .dhJogo(jogo.getDhJogo())
                .nomeEstadio(jogo.getEstadio().getNome())
                .campeonato(jogo.getCampeonato())
                .id(jogo.getId())
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
