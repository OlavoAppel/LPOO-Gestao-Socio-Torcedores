# Relatório de testes manuais

**Área avaliada:** cadastro, login e página de jogos.

Os cadastros padrão, com dados considerados válidos, foram realizados e aprovados. A tabela abaixo registra os casos adicionais verificados manualmente.

| ID | Caso testado | Resultado observado | Situação |
|---|---|---|---|
| CAD-01 | Cadastrar `admin` como username, apesar de já existir uma conta com esse username | O cadastro é aceito, mas o login com o usuário duplicado falha | Falha — corrigida |
| CAD-02 | Informar CPF com mais de 11 números | A página recarrega sem explicar o erro | Falha de validação e de feedback — corrigida |
| CAD-03 | Informar CPF com menos de 11 números | O cadastro é aceito | Falha — corrigida |
| CAD-04 | Informar letras no CPF | O cadastro é aceito | Falha — corrigida |
| CAD-05 | Deixar campos obrigatórios vazios | O cadastro não é liberado | Aprovado |
| CAD-06 | Informar e-mail sem `@` | O cadastro é bloqueado e aparece uma mensagem de erro | Aprovado |
| CAD-07 | Informar e-mail aparentemente incompleto, como `@sll` | O cadastro é aceito | Falha — corrigida |
| CAD-08 | Usar apenas números no username | O cadastro é aceito | Regra a confirmar |
| CAD-09 | Usar espaços no username e criar a conta | O cadastro é aceito, mas não foi possível entrar depois com esse username | Falha — corrigida |
| CAD-10 | Usar uma senha de quatro caracteres especiais, como `!@#$` | O cadastro é aceito | Regra de senha a confirmar |
| CAD-11 | Cadastrar outro usuário com e-mail já utilizado | O cadastro é bloqueado | Aprovado |
| CAD-12 | Informar espaços antes do e-mail | Os espaços são removidos e o cadastro é criado | Comportamento observado |
| LOGIN-01 | Tentar entrar com username ou senha incorretos | O login é recusado e informa credenciais inválidas | Aprovado |
| JOG-01 | Clicar em “Saiba mais” na página de jogos | O link não funciona | Falha — corrigida (o botão deixou de existir no redesenho; cada jogo é um link para `/jogos/{id}`) |

## Principais problemas observados

- A validação do CPF aceita letras e quantidades insuficientes de números. Com números a mais, a página recarrega sem mensagem explicativa.
- É possível cadastrar username duplicado ou com espaços, mas essas contas não podem ser autenticadas corretamente.
- A validação do e-mail aceita um endereço aparentemente incompleto.
- O botão “Saiba mais” na página de jogos não funciona.

## Regras a confirmar

- Se usernames compostos apenas por números devem ser aceitos.
- Qual deve ser o tamanho mínimo e quais caracteres devem ser permitidos nas senhas.

## Capturas de tela



## Observações sobre os testes

- Os resultados acima são de testes manuais exploratórios, realizados no navegador.
- “Aprovado” indica que o sistema se comportou conforme o esperado naquele caso testado; não representa cobertura completa da funcionalidade.
- As falhas foram registradas como observadas no teste original; a coluna "Situação" indica as que já foram corrigidas no código.

## Correções aplicadas

- **CPF** (`CadastroForm` + `@Cpf`): exige 11 números e dígitos verificadores válidos; recusa letras, tamanhos errados e sequências repetidas (`11111111111`). A tela mostra a mensagem junto ao campo.
- **E-mail**: além do `@Email`, exige domínio com ponto (`nome@email.com`); `@sll` e `a@b` são recusados. Espaços nas pontas continuam sendo removidos.
- **Username**: de 3 a 30 caracteres entre letras, números, `.`, `-` e `_` (sem espaços). Espaços nas pontas são removidos.
- **Duplicidade**: `CadastroService` consulta username (sem diferenciar maiúsculas), e-mail e CPF separadamente. A consulta antiga (`checkDuplicateUser`) fazia join com `Associado` e ignorava o `admin`, que não tem associado.
- Testes automatizados das regras do formulário: `CadastroFormValidationTest`.

## Ainda em aberto (aguardam decisão)

- CAD-08: username só com números continua aceito.
- CAD-10: a senha continua sem tamanho mínimo nem restrição de caracteres.
