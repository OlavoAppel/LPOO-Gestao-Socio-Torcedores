# Relatório de testes

## Etapa 1 — Testes manuais no localhost

**Área avaliada:** cadastro, login e página de jogos.

Os testes foram realizados no navegador, navegando pela aplicação no localhost. Os cadastros com dados válidos tiveram o resultado esperado. A tabela abaixo preserva os resultados observados na execução original.

| ID | Caso testado | Resultado observado | Situação |
|---|---|---|---|
| CAD-01 | Cadastrar `admin` como username, apesar de já existir uma conta com esse username | O cadastro é aceito, mas o login com o usuário duplicado falha | Falha |
| CAD-02 | Informar CPF com mais de 11 números | A página recarrega sem explicar o erro | Falha de validação e de feedback |
| CAD-03 | Informar CPF com menos de 11 números | O cadastro é aceito | Falha |
| CAD-04 | Informar letras no CPF | O cadastro é aceito | Falha |
| CAD-05 | Deixar campos obrigatórios vazios | O cadastro não é liberado | Aprovado |
| CAD-06 | Informar e-mail sem `@` | O cadastro é bloqueado e aparece uma mensagem de erro | Aprovado |
| CAD-07 | Informar e-mail aparentemente incompleto, como `@sll` | O cadastro é aceito | Falha |
| CAD-08 | Usar apenas números no username | O cadastro é aceito | Regra a confirmar |
| CAD-09 | Usar espaços no username e criar a conta | O cadastro é aceito, mas não foi possível entrar depois com esse username | Falha |
| CAD-10 | Usar uma senha de quatro caracteres especiais, como `!@#$` | O cadastro é aceito | Regra de senha a confirmar |
| CAD-11 | Cadastrar outro usuário com e-mail já utilizado | O cadastro é bloqueado | Aprovado |
| CAD-12 | Informar espaços antes do e-mail | Os espaços são removidos e o cadastro é criado | Comportamento observado |
| LOGIN-01 | Tentar entrar com username ou senha incorretos | O login é recusado e informa credenciais inválidas | Aprovado |
| JOG-01 | Clicar em “Saiba mais” na página de jogos | O link não funciona | Falha |

## Etapa 2 — Retestes e novos casos

**Área avaliada:** cadastro, página de jogos, compra de ingressos e cadastro de lotes.

Os resultados desta etapa foram apurados pela análise do código da versão `621cc7d`.

| ID | Caso testado | Resultado observado | Situação |
|---|---|---|---|
| CAD-01 | Cadastrar `admin` ou `ADMIN` com a conta administrativa já existente | O cadastro é bloqueado e o campo informa que o usuário já está em uso | Corrigido |
| CAD-02 | Informar CPF com mais de 11 números | O cadastro é bloqueado e o erro aparece junto ao campo CPF | Corrigido |
| CAD-03 | Informar CPF com menos de 11 números | O cadastro é bloqueado e o erro aparece junto ao campo CPF | Corrigido |
| CAD-04 | Informar letras no CPF | O cadastro é bloqueado e o erro aparece junto ao campo CPF | Corrigido |
| CAD-07 | Informar `@sll` ou `a@b` como e-mail | Os dois endereços são recusados pela regra de formato do e-mail | Corrigido |
| CAD-08 | Usar `123456` como username | O valor passa pela validação de formato | Regra a confirmar |
| CAD-09 | Usar `joao silva` como username | O cadastro é bloqueado e o formulário informa que o nome de usuário não pode conter espaços | Corrigido |
| CAD-10 | Usar uma senha curta como `!@#$` | O valor passa pela validação de preenchimento da senha | Regra a confirmar |
| JOG-01 | Selecionar um jogo no calendário para ver os detalhes | O jogo é um link para a página de detalhes correspondente ao seu ID | Corrigido |
| CAD-13 | Informar `11111111111` como CPF | O cadastro é bloqueado porque o CPF contém apenas dígitos repetidos | Aprovado |
| ING-01 | Acessar a página de compra de um jogo sem estar logado | A página exige autenticação e encaminha o visitante para o login. Nenhum ingresso é emitido | Aprovado |
| ING-02 | Tentar comprar um ingresso de um lote aberto com estoque zerado | A compra é bloqueada com a mensagem “Ingressos esgotados para este lote” | Aprovado |
| ING-03 | Tentar comprar sem plano ativo durante a fase exclusiva de um lote com estoque | A compra é recusada e a mensagem informa até quando a venda é exclusiva para sócios | Aprovado |
| ING-04 | Com o limite padrão, tentar comprar outro ingresso para o mesmo jogo usando a mesma conta, em outro setor | A segunda compra é bloqueada porque o limite é por jogo. O estoque do segundo setor não é alterado | Aprovado |
| LOT-01 | Cadastrar um lote com o início da venda no mesmo horário do jogo | O lote não é salvo e o sistema informa que o início da venda deve ser antes do jogo | Aprovado |

## Etapa 3 — Novo teste manual de cadastro

| ID | Caso testado | Resultado observado | Situação |
|---|---|---|---|
| CAD-14 | Cadastrar com CPF válido contendo exatamente 11 dígitos, sem pontos ou traços | O cadastro é recusado com uma mensagem dizendo que o CPF precisa ter 11 dígitos sem pontos ou traços, embora o valor informado já tenha 11 dígitos nesse formato | Falha |

