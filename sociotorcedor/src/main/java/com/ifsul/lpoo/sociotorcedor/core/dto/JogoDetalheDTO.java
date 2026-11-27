package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/** Página de um jogo: dados da partida, setores à venda e a escada de preços por categoria. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JogoDetalheDTO {

    private JogoDTO jogo;

    private List<SetorOfertaDTO> setores;

    private List<CategoriaColunaDTO> categorias;

    private boolean encerrado;

    private String suaCategoria;

    private boolean podeComprar;

    private LocalDateTime proximaAbertura;

    public boolean isAlgumEmVenda() {
        return setores != null && setores.stream().anyMatch(SetorOfertaDTO::isEmVenda);
    }

    public SetorOfertaDTO setor(Long loteId) {
        if (setores == null) {
            return null;
        }
        return setores.stream().filter(s -> Objects.equals(s.getLoteId(), loteId)).findFirst().orElse(null);
    }

}
