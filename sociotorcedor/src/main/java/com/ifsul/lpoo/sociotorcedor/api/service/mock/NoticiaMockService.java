package com.ifsul.lpoo.sociotorcedor.api.service.mock;

import com.ifsul.lpoo.sociotorcedor.api.service.NoticiaService;
import com.ifsul.lpoo.sociotorcedor.core.dto.NoticiaDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * SIMULA as notícias do clube fictício com textos fixos em memória (nada é gravado no banco).
 * As datas são relativas ao dia de hoje para a lista parecer atual.
 *
 * <p>Para virar real faltaria no backend: persistir a notícia (hoje {@code core/model/base/Noticia} é só
 * uma classe, sem tabela) com título, resumo, texto, editoria, data de publicação e autor; um repositório;
 * e telas de admin para escrever e publicar. Aí basta outra implementação de {@link NoticiaService}.
 */
@Service
public class NoticiaMockService implements NoticiaService {

    @Override
    public List<NoticiaDTO> listar() {
        LocalDate hoje = LocalDate.now();
        return List.of(
                new NoticiaDTO(1L,
                        "Torcida ensaia mosaico de 1931 cartões para os 95 anos do clube",
                        "Voluntários da arquibancada norte montam o brasão com um cartão para cada ano desde a fundação, no campo de aviação.",
                        List.of(
                                "O mosaico do aniversário vai ocupar toda a arquibancada norte: são 1931 cartões, um para cada ano desde a fundação do clube, em 1931, no antigo campo de aviação da cidade.",
                                "Os cartões são distribuídos por fileira e por cadeira. Quem chega cedo encontra o seu já preso no assento, com a cor certa voltada para baixo, e só precisa erguê-lo quando o telão der o sinal.",
                                "O desenho é o próprio brasão, com a biruta laranja e branca no centro. Nas duas últimas fileiras, as iniciais do clube fecham a imagem."),
                        hoje.minusDays(1), "Torcida"),
                new NoticiaDTO(2L,
                        "Biruta volta ao alto da cobertura depois da reforma",
                        "O símbolo do clube, retirado durante a troca do telhado da arquibancada, foi reinstalado com tecido novo.",
                        List.of(
                                "A biruta que dá nome ao apelido do clube voltou ao mastro da cobertura nesta semana. A peça antiga, desbotada pelo sol, foi guardada no memorial.",
                                "O tecido novo segue as faixas originais, duas laranja e duas brancas, e pode ser visto de qualquer setor do estádio.",
                                "A reforma da cobertura também trocou a iluminação das rampas de acesso, que agora ficam acesas até a saída do último torcedor."),
                        hoje.minusDays(4), "Clube"),
                new NoticiaDTO(3L,
                        "Sub-17 termina o torneio regional de base sem derrotas",
                        "A equipe venceu as cinco partidas e sofreu só dois gols em toda a competição.",
                        List.of(
                                "Os garotos da base fecharam a campanha com cinco vitórias em cinco jogos. A defesa sofreu apenas dois gols, e o artilheiro da equipe marcou em todas as rodadas.",
                                "Parte do elenco treina com o time principal na próxima semana, numa atividade aberta aos sócios Ouro como parte da visita guiada ao clube.",
                                "A comissão técnica agradeceu às famílias que acompanharam os jogos fora de casa."),
                        hoje.minusDays(7), "Base"),
                new NoticiaDTO(4L,
                        "Como funciona o desconto de sócio na compra do ingresso",
                        "O desconto da sua categoria é aplicado em todos os setores, no momento da compra.",
                        List.of(
                                "Quem é sócio paga menos em todos os setores do estádio. O desconto depende da categoria: quanto mais alta, maior a porcentagem.",
                                "Na página de cada jogo, a tabela de preços mostra quanto cada categoria paga em cada setor. Quando você está logado, a coluna da sua categoria aparece em destaque.",
                                "O desconto entra automaticamente ao comprar o ingresso, sem cupom. O valor pago fica registrado em Minha conta, junto com o código de entrada."),
                        hoje.minusDays(10), "Sócio"),
                new NoticiaDTO(5L,
                        "Sócios Ouro fazem a primeira visita guiada da temporada",
                        "O roteiro passou pelo vestiário, pelo memorial e terminou no gramado.",
                        List.of(
                                "A primeira turma da visita guiada da temporada conheceu os bastidores do clube: vestiário, sala de imprensa, memorial e a beira do gramado.",
                                "No memorial, a peça mais fotografada foi a biruta original de 1931, que ficou no mastro do campo de aviação até a construção do estádio.",
                                "A visita guiada é um dos benefícios da categoria Ouro e acontece uma vez por temporada para cada sócio."),
                        hoje.minusDays(15), "Sócio")
        ).stream().sorted(Comparator.comparing(NoticiaDTO::getData).reversed()).toList();
    }

    @Override
    public List<NoticiaDTO> recentes(int quantidade) {
        return listar().stream().limit(quantidade).toList();
    }

    @Override
    public Optional<NoticiaDTO> buscar(Long id) {
        return listar().stream().filter(n -> Objects.equals(n.getId(), id)).findFirst();
    }

}
