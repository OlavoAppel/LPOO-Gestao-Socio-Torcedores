# Dados fictícios do front

Alguns recursos do front ainda não existem no backend. Para a tela não ficar presa a isso, eles usam dados
fictícios atrás de uma interface (ver a decisão "Interface com implementação de demonstração" em
[`docs/padroes/README.md`](../padroes/README.md)). Regras seguidas:

- Nada fictício é gravado no banco e nenhuma entidade foi criada.
- O controller depende só da interface; a implementação de demonstração fica em `api/service/mock/`.
- Nenhuma ação fictícia fica disponível: botões de recursos sem backend aparecem desabilitados, com o motivo ao lado.
- Todo o conteúdo é do clube fictício (Esporte Clube Ventania): nada de notícias, jogadores ou placares reais.

Este arquivo é o roteiro do que falta no backend para cada recurso virar real.

| Recurso | Interface e implementação | Telas que usam | O que o backend precisaria para virar real |
|---|---|---|---|
| Notícias do clube | `api/service/NoticiaService` e `api/service/mock/NoticiaMockService` | `/` (3 mais recentes), `/noticias`, `/noticias/{id}` | **Entidade** `Noticia` persistida (hoje `core/model/base/Noticia` é só uma classe, sem `@Entity` nem tabela), seguindo o padrão de nomes de coluna: `NOT_ID`, `NOT_TITULO`, `NOT_RESUMO`, `NOT_TEXTO`, `NOT_EDITORIA`, `NOT_DATAPUBLICACAO`, `NOT_AUTOR` (FK para `USUARIOS`). **Repositório** com `findAllByOrderByDataPublicacaoDesc` e um limite para a página inicial. **Regras**: só notícias publicadas aparecem; data de publicação não pode ser futura para aparecer. **Telas de admin** para escrever, publicar e despublicar (escrita, ainda inexistente). |
| Programa de pontos (prévia) | `api/service/FidelidadeService` e `api/service/mock/FidelidadeMockService` | `/sociotorcedor`, seção "Pontos" (só para sócios) | **Entidade** de movimento: `MovimentoPontos` (associado, data, origem, pontos, referência ao ingresso ou mensalidade). **Entidade** de recompensa: nome, descrição, custo em pontos, estoque, ativa. **Regras de pontuação**: quanto vale mensalidade paga em dia, presença no jogo (exige um check-in que hoje não existe) e compras; se pontos expiram. **Resgate**: escrita que valida saldo e estoque, debita os pontos e registra a retirada. Com isso, a implementação real devolve `PontosDTO.previa = false` e a tela deixa de mostrar o aviso de prévia. |

## O que é real (e não fictício) nas telas novas

Para não haver dúvida na revisão, estes dados vêm do banco, por consultas de leitura criadas para o front:

| Dado | De onde vem |
|---|---|
| Página do jogo e escada de preços por categoria | `JogoDetalheService`: lotes do jogo, validade pela cadeia de validação (`LoteIngressoService.findLotes`) e preços por `CategoriaSocio.aplicarDescontoIngresso`, o mesmo cálculo da compra |
| Mosaico da torcida (um quadrado por sócio) | `AssociadoRepository.listarMembrosDoMosaico`: sócios ativos na ordem de adesão |
| Carteirinha digital | Nome, categoria, número (`Associado.id`) e data de adesão do sócio logado, mais a posição dele no mosaico |
| Histórico da associação | `HistoricoCategoriaRepository.buscarDoAssociado` |
| Comprovante da compra | `IngressoRepository.buscarDoUsuarioPorCompraRecente` |
| Painel do admin | `JogoRepository.resumoPorJogo` (a consulta das métricas, sem o filtro de data) e os lotes de cada jogo |

## Como trocar uma implementação de demonstração pela real

1. Crie a classe real implementando a mesma interface (por exemplo `NoticiaBancoService implements NoticiaService`) e anote com `@Service`.
2. Apague a classe de `api/service/mock/` (ou tire o `@Service` dela), para o Spring encontrar uma única implementação.
3. Controllers e templates não mudam. Se o recurso tiver ação de escrita (resgate de pontos, publicar notícia), crie o endpoint e troque o botão desabilitado por um formulário.
