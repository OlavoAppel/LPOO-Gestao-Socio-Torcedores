# LPOO - Sistema de Gestão Sócio-Torcedores

Projeto desenvolvido para a disciplina de Linguagem de Programação Orientada a Objetos (LPOO) do curso de Ciência da Computação no IFSul.

## Proposta do Projeto

O objetivo deste projeto é desenvolver um Sistema de Gestão para Sócio-Torcedor focado em administrar de forma eficiente o engajamento, o controle financeiro e as vantagens oferecidas aos associados de um clube. O software contemplará as seguintes funcionalidades principais:

* **Gestão de Associados:** Cadastro de novos associados e sistema para atualização contínua e migração de planos.
* **Categorização e Descontos:** Estruturação em diferentes classes de sócios, garantindo a aplicação de regras específicas de preços e descontos automáticos para cada categoria.
* **Controle de Ingressos:** Definição de valores dinâmicos para cada campeonato, gerenciando o fluxo de reserva e retirada das entradas para os jogos.
* **Programa de Benefícios:** Sistema de controle e logística para a liberação, resgate e retirada de brindes (como copos e camisetas) e experiências exclusivas (como visitas guiadas ao clube).

## Como rodar

Pré-requisitos: JDK 26 e PostgreSQL em `localhost:5432`. Defina as variáveis `DB_NAME`, `DB_USER` e `DB_PASSWORD` e, dentro de `sociotorcedor/`, rode:

```
./mvnw spring-boot:run     # app em http://localhost:8080
./mvnw test
```

Em banco vazio, o `DataSeeder` cria jogos, lotes, categorias de sócio e usuários de teste (as credenciais estão no próprio seeder).

## Front-end

As páginas são montadas no servidor com Thymeleaf e estilizadas com **Tailwind CSS v4**, sem Bootstrap: a identidade visual pesa mais aqui, e o visual pronto do Bootstrap custaria mais para sobrescrever. O clube é fictício: **Esporte Clube Ventania** (fundado em 1931 num campo de aviação, daí a biruta no brasão). A ideia visual é o **mosaico da torcida**: cada sócio é um quadrado, como os cartões que a arquibancada ergue para formar o brasão.

### CSS: Tailwind compilado pelo CLI standalone

O CSS é gerado pelo **CLI standalone do Tailwind v4.3.4** (um binário único, sem Node) e o resultado, `static/css/app.css`, fica no repositório. Escolhemos compilar porque a página chega estilizada mesmo sem JavaScript e o celular do torcedor não paga a compilação no navegador; o custo é rodar o comando abaixo quando mudar classes nos templates.

1. Baixe o binário do seu sistema em [github.com/tailwindlabs/tailwindcss/releases/tag/v4.3.4](https://github.com/tailwindlabs/tailwindcss/releases/tag/v4.3.4) (ex.: `tailwindcss-windows-x64.exe`, `tailwindcss-linux-x64`, `tailwindcss-macos-arm64`) e deixe-o fora do repositório.
2. Dentro de `sociotorcedor/`, rode:

```
tailwindcss -i src/main/tailwind/app.css -o src/main/resources/static/css/app.css --minify
```

Enquanto edita templates, acrescente `--watch` para recompilar a cada mudança. Rodar só a aplicação não exige o binário: o `app.css` já vem pronto.

### Onde fica cada coisa

- `static/css/paleta.css`: **único lugar da paleta**. Tokens semânticos (`--bg`, `--surface`, `--ink`, `--ink-muted`, `--line`, `--brand`, `--accent`, `--danger`, `--success`, `--nivel-1..3`, `--chart-*`), cada um com valor claro e escuro (`light-dark()`), e uma segunda paleta comentada como exemplo. Por ser CSS puro, trocar cores não exige recompilar.
- `src/main/tailwind/app.css`: entrada do Tailwind (liga os tokens aos utilitários, escala de tipos, componentes como `.botao`, `.campo`, `.pastilha`).
- `templates/fragments/marca.html` e `messages.properties`: brasão (vetor e mosaico) e nome do clube. O ícone da aba (`static/images/brasao.svg`) é uma cópia do brasão.
- `templates/fragments/`: topo e abas (`navegacao`), rodapé, jogo, mosaico, planos, ingresso, carteirinha, ícones e peças de interface (`ui`).
- `static/js/theme.js`: único JavaScript. Alterna `<html data-theme="light|dark">` e salva a escolha; sem escolha salva, segue o sistema. Sem JavaScript, as cores seguem o sistema e todas as tarefas continuam funcionando.
- Gráficos do admin: HTML e CSS feitos no servidor, com as cores dos tokens (não há biblioteca de gráficos).
- Dados fictícios (notícias e programa de pontos): interfaces em `api/service` com implementações em `api/service/mock/`. O roteiro do que falta no backend está em [`docs/front/dados-ficticios.md`](docs/front/dados-ficticios.md).

### Mapa de telas

| Tela | Rota | Acesso |
|---|---|---|
| Início: próximo jogo, brasão em mosaico, próximos jogos, mosaico da torcida, notícias | `GET /` | público |
| Calendário por mês | `GET /jogos` | público |
| Jogo: setores, estoque e preço de cada categoria | `GET /jogos/{id}` | público (nova) |
| Planos de sócio: comparação e mosaico | `GET /planos` | público (nova) |
| Notícias do clube (fictícias) | `GET /noticias`, `GET /noticias/{id}` | público (novas) |
| Entrar / Criar conta | `GET /login`, `GET/POST /cadastro` | público |
| Comprar ingresso (escolha do setor) | `GET /ingressos?jogoId=`, `POST /ingressos/comprar` | logado |
| Ingresso comprado (comprovante com código) | `GET /ingressos/sucesso` | logado |
| Minha conta: ingressos, dados, sair | `GET /perfil` | `USER` e `ADMIN` |
| Minha associação: carteirinha, benefícios, pontos (prévia), histórico; ou escolha do plano | `GET /sociotorcedor`, `POST /sociotorcedor/associar`, `POST /sociotorcedor/revogarassociacao` | logado |
| Administração: números do mês e jogos com situação dos lotes | `GET /admin` | `ADMIN` |
| Métricas | `GET /admin/metricas` | `ADMIN` |
| Cadastrar jogo | `GET/POST /admin/cadastro/jogo` | `ADMIN` |
| Lotes do jogo | `GET/POST /admin/cadastro/{jogoId}/lotes` | `ADMIN` |

## Membros da Equipe e Atribuições

- **Allan Theodoro** ([@allan-thro](https://github.com/allan-thro))
  - **Atribuição:** Desenvolvimento do Backend e Frontend do sistema.

- **Bernardo Zavistanovicz Deters** ([@bedeters](https://github.com/bedeters))
  - **Atribuição:** Auxílio na modelagem do banco de dados.

- **Olavo Appel Neto** ([@OlavoAppel](https://github.com/OlavoAppel))
  - **Atribuição:** Realização dos testes do sistema e modelagem do banco de dados.

- **Thomas Cansian dos Santos** ([@Thcansian](https://github.com/Thcansian))
  - **Atribuição:** Auxílio no desenvolvimento do Backend e na realização dos testes do sistema.

## Créditos

- **Escudos dos times** (`sociotorcedor/src/main/resources/static/images/escudos/`): imagens obtidas da [Wikipédia](https://www.wikipedia.org/) e do [Wikimedia Commons](https://commons.wikimedia.org/), a partir da página de cada clube. Os escudos são marcas registradas dos respectivos clubes e foram usados aqui apenas para fins educacionais, sem fins lucrativos. As imagens foram convertidas para PNG e redimensionadas para no máximo 256 px.
- **Times cadastrados:** os 20 clubes do Campeonato Brasileiro Série A 2026 e os 32 clubes da fase de grupos da Copa Libertadores 2026 (dados da [Wikipédia](https://en.wikipedia.org/wiki/2026_Campeonato_Brasileiro_S%C3%A9rie_A) e da [Wikipédia](https://en.wikipedia.org/wiki/2026_Copa_Libertadores_group_stage)).

- **Fontes:** Sofia Sans e Sofia Sans Extra Condensed, via [Google Fonts](https://fonts.google.com/) (SIL Open Font License).
- **Brasão do Esporte Clube Ventania:** SVG original, feito para o projeto (vetor e mosaico). O clube, as notícias e o programa de pontos são fictícios.

## Relatório de testes

O relatório de testes manuais e bugs encontrados está em [`testes/README.md`](testes/README.md).
