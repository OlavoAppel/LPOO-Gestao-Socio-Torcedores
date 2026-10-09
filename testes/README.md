# Relatório de testes manuais

**Área avaliada:** cadastro, login e página de jogos.

Os cadastros padrão, com dados considerados válidos, foram realizados e aprovados. A tabela abaixo registra os casos adicionais verificados manualmente.

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
- As falhas estão registradas como observadas, sem indicar que foram corrigidas.
