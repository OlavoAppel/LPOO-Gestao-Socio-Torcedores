package com.ifsul.lpoo.sociotorcedor.core.factory;

import com.ifsul.lpoo.sociotorcedor.core.dto.AdminJogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.BeneficioSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.CarteirinhaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.CategoriaColunaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.CategoriaSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoJogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDetalheDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.LinhaBeneficioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.LoteIngressoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.MosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.NivelMosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.PrecoCategoriaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.SetorOfertaDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.TimeDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.base.BeneficioSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.CategoriaSocio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.validation.ValidationResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                .nome(jogo.getCasa().getNome())
                .build();

        TimeDTO timeFora = TimeDTO.builder()
                .sigla(jogo.getFora().getSigla())
                .escudoPath(jogo.getFora().getEscudoPath())
                .nome(jogo.getFora().getNome())
                .build();

        jogoDTO.setCasa(timeCasa);
        jogoDTO.setFora(timeFora);

        return jogoDTO;
    }

    // ------------------------------------------------------------------ telas do front (só leitura)

    /**
     * Um lote como o torcedor vê: preço de cada categoria e o da categoria dele. Os preços usam o mesmo
     * CategoriaSocio.aplicarDescontoIngresso da compra; a validade vem do resultado da cadeia de validação.
     */
    public static SetorOfertaDTO createSetorOfertaDTO(LoteIngresso lote, LoteIngressoDTO situacao,
                                                      List<CategoriaSocio> categorias, CategoriaSocio sua,
                                                      LocalDateTime agora) {
        BigDecimal base = lote.getPrecoBase();
        List<PrecoCategoriaDTO> precos = categorias.stream()
                .map(c -> new PrecoCategoriaDTO(
                        c.getId(),
                        c.getNome(),
                        c.getHierarquia(),
                        base == null ? null : c.aplicarDescontoIngresso(base),
                        sua != null && Objects.equals(c.getId(), sua.getId())))
                .toList();

        boolean emVenda = situacao != null && Boolean.TRUE.equals(situacao.valido());
        LocalDateTime inicio = lote.getDhInicioVenda();

        return SetorOfertaDTO.builder()
                .loteId(lote.getId())
                .setor(lote.getSetor() != null ? lote.getSetor().getNome() : lote.getNome())
                .precoBase(base)
                .precos(precos)
                .seuPreco(sua == null || base == null ? null : sua.aplicarDescontoIngresso(base))
                .disponiveis(lote.getIngressoDisponiveis())
                .total(lote.getIngressosTotaisLote())
                .emVenda(emVenda)
                .motivo(emVenda || situacao == null ? null : situacao.motivo())
                .vendaAbreEm(inicio != null && inicio.isAfter(agora) ? inicio : null)
                .build();
    }

    public static JogoDetalheDTO createJogoDetalheDTO(Jogo jogo, List<SetorOfertaDTO> setores,
                                                      List<CategoriaSocio> categorias, CategoriaSocio sua,
                                                      boolean podeComprar, LocalDateTime agora) {
        List<CategoriaColunaDTO> colunas = categorias.stream()
                .map(c -> new CategoriaColunaDTO(c.getNome(), c.getHierarquia(),
                        sua != null && Objects.equals(c.getId(), sua.getId())))
                .toList();

        LocalDateTime proximaAbertura = setores.stream()
                .map(SetorOfertaDTO::getVendaAbreEm)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

        return JogoDetalheDTO.builder()
                .jogo(createJogoDTO(jogo))
                .setores(setores)
                .categorias(colunas)
                .encerrado(jogo.getDhJogo() != null && jogo.getDhJogo().isBefore(agora))
                .suaCategoria(sua == null ? null : sua.getNome())
                .podeComprar(podeComprar)
                .proximaAbertura(proximaAbertura)
                .build();
    }

    /** Linha do painel do admin. O resumo de vendas pode ser nulo (jogo sem nenhum dado de venda). */
    public static AdminJogoDTO createAdminJogoDTO(Jogo jogo, DashboardInfoJogoDTO resumo, LocalDateTime agora) {
        int setoresNoEstadio = jogo.getEstadio() == null || jogo.getEstadio().getSetores() == null
                ? 0 : jogo.getEstadio().getSetores().size();
        int lotes = jogo.getLoteIngressoList() == null ? 0 : jogo.getLoteIngressoList().size();

        return AdminJogoDTO.builder()
                .jogo(createJogoDTO(jogo))
                .lotes(lotes)
                .setoresNoEstadio(setoresNoEstadio)
                .vendidos(resumo == null ? 0L : resumo.getIngressosVendidos())
                .ocupacao(resumo == null ? BigDecimal.ZERO : resumo.getOcupacaoJogoPorcentagem())
                .receita(resumo == null ? BigDecimal.ZERO : resumo.getReceitaPorJogo())
                .encerrado(jogo.getDhJogo() != null && jogo.getDhJogo().isBefore(agora))
                .build();
    }

    /**
     * Benefícios lado a lado para a comparação de planos, na ordem em que aparecem nas categorias.
     * Os de desconto em ingresso ficam de fora: a tabela já tem a linha do desconto de cada categoria.
     */
    public static List<LinhaBeneficioDTO> createLinhasBeneficio(List<CategoriaSocioDTO> categorias) {
        Map<String, LinhaBeneficioDTO> linhas = new LinkedHashMap<>();
        for (int i = 0; i < categorias.size(); i++) {
            List<BeneficioSocioDTO> beneficios = categorias.get(i).getBeneficios();
            if (beneficios == null) {
                continue;
            }
            for (BeneficioSocioDTO beneficio : beneficios) {
                if (beneficio.getDescontoIngresso() != null && beneficio.getDescontoIngresso().signum() > 0) {
                    continue;
                }
                LinhaBeneficioDTO linha = linhas.computeIfAbsent(beneficio.getNome(), nome -> new LinhaBeneficioDTO(
                        nome, beneficio.getDescricao(), new ArrayList<>(Collections.nCopies(categorias.size(), false))));
                linha.getPorCategoria().set(i, true);
            }
        }
        return new ArrayList<>(linhas.values());
    }

    /** Carteirinha digital: dados do cadastro do sócio e a posição dele no mosaico da torcida. */
    public static CarteirinhaDTO createCarteirinhaDTO(Associado associado, MosaicoDTO mosaico) {
        CategoriaSocio categoria = associado.getCategoria();
        int nivel = mosaico.getNiveis().stream()
                .filter(n -> Objects.equals(n.getHierarquia(), categoria.getHierarquia()))
                .map(NivelMosaicoDTO::getNivel)
                .findFirst()
                .orElse(1);

        return CarteirinhaDTO.builder()
                .nome(associado.getNome())
                .categoria(categoria.getNome())
                .nivel(nivel)
                .numero(associado.getId())
                .socioDesde(associado.getDataAdesao())
                .posicaoMosaico(mosaico.getSuaPosicao())
                .totalMosaico(mosaico.getTotalSocios())
                .build();
    }

}
