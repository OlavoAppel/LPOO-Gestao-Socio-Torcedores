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

As páginas são montadas no servidor com Thymeleaf e estilizadas com **Tailwind CSS v4**. O Tailwind foi escolhido no lugar do Bootstrap porque a identidade visual pesa mais aqui, e o visual pronto do Bootstrap custaria mais para sobrescrever. A repetição de markup fica nos fragmentos do Thymeleaf.

- **Sem build:** o projeto não tem Node. O Tailwind vem do CDN (`@tailwindcss/browser@4.1.14`, versão fixa) e compila no navegador. Não é preciso nenhum comando além do `spring-boot:run`, mas a página precisa de internet para carregar o Tailwind e as fontes.
- **Onde fica cada coisa:**
  - `templates/fragments/head.html`: fontes, script de tema, Tailwind, `@theme` e os componentes (`.btn`, `.entrada`, `.ingresso`, `.led`...).
  - `static/css/tema.css`: **único lugar da paleta**. Tokens semânticos (`--bg`, `--surface`, `--ink`, `--brand`, `--accent`, `--chart-*`...) com valor claro e escuro, e uma paleta alternativa comentada como exemplo.
  - `templates/fragments/marca.html` e `messages.properties`: brasão e nome do clube fictício (Lagoa Esporte Clube).
  - `templates/fragments/`: navbar, rodapé, placar e linha de jogo, carteirinha, alertas e estados vazios.
- **Tema claro/escuro:** `<html data-theme="light|dark">`, alternado pelo botão da navbar (`static/js/theme.js`). Nos templates só entram cores semânticas (`bg-surface`, `text-ink`, `bg-brand`...). As cores padrão do Tailwind estão desligadas, então trocar a paleta não exige editar HTML.
- **Gráficos do admin:** Chart.js (CDN). `static/js/dashboard.js` lê as cores de `tema.css` e redesenha ao trocar o tema.

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

- **Fontes:** Saira e Atkinson Hyperlegible Next, via [Google Fonts](https://fonts.google.com/) (SIL Open Font License).
- **Brasão do Lagoa Esporte Clube:** SVG original, feito para o projeto. O clube é fictício.

## Relatório de testes

O relatório de testes manuais e bugs encontrados está em [`testes/README.md`](testes/README.md).
