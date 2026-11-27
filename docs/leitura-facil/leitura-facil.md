# Sistema de Sócio-Torcedores

## Versão em Leitura Fácil

Este texto explica o nosso projeto de um jeito simples.

Ele é para quem não conhece programação.

Outras versões deste documento:

- [Documentação completa em português](../pt-br/documentacao.md)
- [Documentation in English](../en/documentation.md)

---

## 1. O que é este projeto?

Este projeto é um **sistema de computador**.

Um sistema é um programa que ajuda a fazer uma tarefa.

O nosso sistema ajuda um **clube de futebol**.

O clube tem torcedores que pagam uma mensalidade.

Esses torcedores se chamam **sócio-torcedores**.

O sistema ajuda o clube a cuidar deles.

## 2. Quem fez o projeto?

Quatro estudantes de Ciência da Computação fizeram o projeto.

Eles estudam no **IFSul**.

O projeto é um trabalho da disciplina **LPOO**.

LPOO quer dizer Linguagem de Programação Orientada a Objetos.

Os estudantes são:

- Allan Theodoro
- Bernardo Zavistanovicz Deters
- Olavo Appel Neto
- Thomas Cansian dos Santos

## 3. O que o sistema faz hoje?

O sistema já faz estas coisas:

- **Cadastrar uma pessoa.** A pessoa escreve o nome, o nome de usuário, o e-mail, a senha e o CPF. O sistema confere se os dados estão certos. Ele mostra o erro ao lado do campo.
- **Entrar no sistema.** A pessoa usa o nome de usuário e a senha. Isso se chama **login**.
- **Sair do sistema.** Isso se chama **logout**.
- **Mostrar os jogos do ano.** A pessoa escolhe um mês. O sistema mostra os jogos desse mês.
- **Mostrar o próximo jogo.**
- **Mostrar os lotes de ingressos de um jogo.**
- **Vender ingressos.** A pessoa escolhe o setor e compra. O pagamento é de mentira, só para teste.
- **Dar desconto.** Cada tipo de sócio paga um preço diferente.
- **Virar sócio.** A pessoa escolhe um plano. Ela vê a carteirinha de sócio. Ela pode cancelar depois.
- **Ajudar o administrador.** O administrador cadastra jogos e lotes. Ele vê números e gráficos.

Um **lote** é um grupo de ingressos.

Cada lote tem um preço e um período de venda.

O sistema avisa se o lote está aberto ou fechado para venda.

O sistema também mostra notícias e pontos de fidelidade. Esses dados são **de mentira**, só para mostrar como a tela vai ficar.

## 4. O que o sistema ainda não faz?

Algumas coisas ainda não funcionam:

- Cobrar o pagamento de verdade.
- Cancelar um ingresso.
- Cobrar a mensalidade do sócio.
- Entregar brindes aos sócios.
- Guardar notícias e pontos no banco de dados.

Os estudantes querem fazer essas coisas depois.

## 5. Quem pode usar cada tela?

Uma **tela** é uma página que a pessoa vê no navegador.

| Tela | Quem pode ver |
|---|---|
| Início | Todas as pessoas |
| Jogos e página de cada jogo | Todas as pessoas |
| Planos | Todas as pessoas |
| Notícias | Todas as pessoas |
| Entrar | Todas as pessoas |
| Cadastro | Todas as pessoas |
| Comprar ingresso | Quem fez login |
| Minha conta | Quem fez login |
| Minha associação | Quem fez login |
| Administração | Só o administrador |

O **administrador** é a pessoa que cuida do sistema.

## 6. Como o sistema é feito?

O sistema tem **partes**.

Cada parte tem um trabalho.

Essas partes se chamam **camadas**.

| Camada | Trabalho dela |
|---|---|
| Telas | Mostrar as páginas para a pessoa |
| Serviços | Seguir as regras do clube |
| Dados | Guardar e buscar as informações |

Veja como as camadas trabalham juntas:

```mermaid
flowchart LR
A["1. A pessoa<br/>abre a tela"] --> B["2. O sistema<br/>recebe o pedido"]
B --> C["3. O sistema<br/>aplica as regras"]
C --> D["4. O sistema<br/>guarda ou busca<br/>os dados"]
D --> E["5. A tela<br/>mostra a resposta"]
```

*Arquivo-fonte e imagem: [facil-fluxo.mmd](../diagramas/facil-fluxo.mmd) · [facil-fluxo.png](../diagramas/facil-fluxo.png)*

Esta forma de organizar o sistema se chama **arquitetura em camadas**.

Outra forma usada é o **MVC**.

MVC divide o sistema em três partes: **Modelo**, **Visão** e **Controle**.

- O **Modelo** guarda as informações.
- A **Visão** é o que a pessoa vê.
- O **Controle** recebe o pedido da pessoa.

## 7. O que são padrões de projeto?

Um **padrão de projeto** é uma boa solução que muita gente já usou.

É como uma receita de bolo.

A receita ajuda a resolver um problema comum.

Nós usamos cinco padrões:

1. **Cadeia de Responsabilidade.** O sistema confere o lote em passos. Cada passo é uma regra. Hoje temos seis regras: o período de venda, o estoque, a venda só para sócios, o plano mínimo, os planos aceitos no setor e o limite de ingressos por jogo.
2. **Método Modelo.** Todas as regras seguem o mesmo caminho.
3. **Fábrica.** Uma parte do sistema só faz objetos. Por exemplo, ela monta o cadastro de uma pessoa e esconde a senha.
4. **Construtor.** O sistema monta objetos grandes com calma, um campo de cada vez.
5. **Repositório.** Uma parte do sistema só conversa com o banco de dados.

O **banco de dados** é o lugar onde o sistema guarda as informações.

## 8. Como a pessoa usa o sistema?

Estes são os passos de uma pessoa nova:

1. A pessoa abre a página inicial.
2. A pessoa clica em **Entrar**.
3. A pessoa clica em **Cadastre-se**.
4. A pessoa preenche o cadastro.
5. A pessoa faz o login.
6. A pessoa clica em **Jogos**.
7. A pessoa vê o calendário.
8. A pessoa clica em um jogo e vê os setores e os preços.
9. A pessoa clica em **Comprar ingresso** e escolhe o setor.
10. A pessoa vê o comprovante com o código de entrada.

## 9. O que tem nas telas?

As telas têm estas partes:

- **Botões.** A pessoa clica neles.
- **Campos.** A pessoa escreve neles.
- **Links.** A pessoa clica e vai para outra tela.
- **Cartões.** Eles mostram um jogo ou um lote.
- **Abas.** No celular, elas ficam fixas embaixo da tela.
- **Escudos.** São as imagens dos times.

Quando a pessoa clica, o sistema responde.

Isso se chama **evento**.

Exemplo: a pessoa clica no mês de **outubro**.

O sistema mostra só os jogos de outubro.

## 10. O sistema foi testado?

Sim. Os estudantes fizeram **14 testes** à mão.

Veja o resultado:

| Resultado | Quantidade |
|---|---|
| Deu certo | 4 |
| Deu erro, e já foi corrigido | 7 |
| Falta decidir a regra | 2 |
| Só observamos | 1 |

Os estudantes também escreveram **testes automáticos**.

Um teste automático é um programa que confere se outro programa funciona.

Eles conferem o formulário de cadastro e as regras dos lotes.

## 11. O que vem depois?

Os próximos passos são:

1. Decidir as regras de nome de usuário e de senha.
2. Cobrar o pagamento do ingresso de verdade.
3. Guardar as notícias e os pontos no banco de dados.
4. Fazer mais testes.

## 12. Palavras difíceis

| Palavra | O que quer dizer |
|---|---|
| Sistema | Programa que ajuda a fazer uma tarefa |
| Sócio-torcedor | Torcedor que paga mensalidade ao clube |
| Login | Entrar no sistema com nome e senha |
| Camada | Parte do sistema com um trabalho só |
| Padrão de projeto | Boa solução para um problema comum |
| Banco de dados | Lugar onde o sistema guarda informações |
| Evento | Resposta do sistema a um clique |
| Lote | Grupo de ingressos com preço e período de venda |
