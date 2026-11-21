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
    /** Do setor mais barato ao mais caro. */
    private List<SetorOfertaDTO> setores;
    /** Colunas da escada de preços, na mesma ordem de SetorOfertaDTO.precos. */
    private List<CategoriaColunaDTO> categorias;
    /** O horário do jogo já passou. */
    private boolean encerrado;
    /** Nome da categoria do torcedor logado; nulo para visitante e admin. */
    private String suaCategoria;
    /** Logado com cadastro de torcedor (o admin não tem e não compra). */
    private boolean podeComprar;
    /** A abertura de venda mais próxima entre os setores que ainda não abriram. */
    private LocalDateTime proximaAbertura;

    public boolean isAlgumEmVenda() {
        return setores != null && setores.stream().anyMatch(SetorOfertaDTO::isEmVenda);
    }

    /** Setor de um lote (a tela de compra percorre os lotes e busca aqui o resto dos dados). */
    public SetorOfertaDTO setor(Long loteId) {
        if (setores == null) {
            return null;
        }
        return setores.stream().filter(s -> Objects.equals(s.getLoteId(), loteId)).findFirst().orElse(null);
    }

}
