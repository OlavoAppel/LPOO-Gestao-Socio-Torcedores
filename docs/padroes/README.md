# Padrões e decisões de arquitetura

## Resumo

| Abordagem | Classificação | Onde |
|---|---|---|
| Factory de entidades | Padrão criacional | `UsuarioFactory`, no cadastro |
| Factory/Mapper de DTOs | Padrão criacional aplicado à conversão | `DTOFactory`, em jogos e lotes |
| Chain of Responsibility | Padrão comportamental | Validação dos lotes de ingresso |
| MVC e camadas | Arquitetura | Controllers, services, repositories, models e templates |
| Repository | Padrão de acesso a dados | Interfaces Spring Data JPA |
| Injeção de dependência e beans singleton | Recursos de framework / princípios de projeto | Beans Spring (`@Service`, `@Component`, `@Bean`) |
| Service Locator com campos estáticos | Padrão de acesso a serviços, usado como conveniência | `InjectionProvider` |
| Interface com implementação de demonstração | Programação para interface (inversão de dependência) | `NoticiaService` e `FidelidadeService`, com implementações em `api/service/mock/` |

## Decisão: Factory para criar usuário e associado

**Contexto:** o cadastro recebe um `CadastroForm`, mas a persistência usa objetos de domínio `Usuario` e `Associado`. A montagem exige distribuir dados entre os dois objetos, definir a categoria inicial e codificar a senha.

**Decisão:** centralizar essa montagem em `UsuarioFactory`, injetada no serviço de cadastro.

**Justificativa:** evita repetir a sequência de instanciação e atribuições em cada ponto que precise criar um usuário novo. Mantém em um local as regras iniciais de construção, inclusive o valor padrão da categoria e a codificação da senha. Se a forma de inicializar o novo usuário mudar, há um ponto principal para revisar.

**Exemplo — `core/factory/UsuarioFactory.java`:**

```java
public Usuario createNewUsuario(CadastroForm form) {
    Associado associado = new Associado();
    Usuario usuario = new Usuario();

    associado.setCategoria(naoAssociado); // CategoriaSocio com hierarquia 0, buscada no repository
    associado.setNome(form.getNome());
    associado.setTelefone(form.getTelefone());
    associado.setCpf(form.getCpf());
    associado.setDataNascimento(form.getDataNascimento());

    usuario.setEmail(form.getEmail());
    usuario.setUsername(form.getUsername());
    usuario.setPassword(passwordEncoder.encode(form.getSenha()));
    usuario.setRole("USER");
    usuario.setAssociado(associado);
    return usuario;
}
```

O serviço usa a fábrica antes de salvar o resultado (`api/service/CadastroService.java`):

```java
Usuario usuario = usuarioFactory.createNewUsuario(form);
InjectionProvider.getUsuarioRepository().save(usuario);
```

**Limite importante:** a Factory constrói o objeto; ela não evita inserções duplicadas nem realiza a persistência. A consulta de duplicidade e o `save` continuam sendo responsabilidade do fluxo de cadastro. O benefício é evitar duplicar a lógica de construção, não reduzir o número de registros no banco.

## Decisão: Factory/Mapper para converter entidades em DTOs

**Contexto:** os dados de domínio (`Jogo`, `LoteIngresso`) não têm exatamente o formato necessário às páginas. Os DTOs expõem campos específicos e agregam dados relacionados, como siglas dos times e o resultado da validação de um lote.

**Decisão:** concentrar a criação desses objetos de resposta em métodos de `DTOFactory` e manter os serviços responsáveis por buscar/processar os dados e chamar a conversão.

**Justificativa:** evita espalhar a cópia e composição dos mesmos campos pelos fluxos que apresentam jogos e lotes. Também separa o formato apresentado ao restante da aplicação da entidade persistida.

**Exemplo — conversão de jogo em `core/factory/DTOFactory.java`:**

```java
public static JogoDTO createJogoDTO(Jogo jogo) {
    JogoDTO jogoDTO = JogoDTO.builder()
            .dhJogo(jogo.getDhJogo())
            .nomeEstadio(jogo.getEstadio().getNome())
            .campeonato(jogo.getCampeonato())
            .build();
    // Também constrói os DTOs dos times da casa e visitante.
    return jogoDTO;
}
```

**Exemplo — uso em `api/service/JogoService.java`:**

```java
JogoDTO jogoDTO = DTOFactory.createJogoDTO(jogo);
jogosDisponiveis.addJogo(jogoDTO);
```

Há também duas sobrecargas para criar `LoteIngressoDTO`: uma com o resultado de validação e outra para o caso de erro de consulta. `LoteService` as utiliza ao montar a lista apresentada.

**Limite:** `DTOFactory` é uma classe utilitária com métodos estáticos, não um bean Spring. Se as conversões passarem a exigir dependências ou regras configuráveis, a implementação pode evoluir para uma fábrica injetável ou um mapper dedicado, atualizando esta decisão.

## Decisão: Chain of Responsibility na validação dos lotes

**Contexto:** a disponibilidade de um lote depende de regras de negócio e a consulta pode precisar executar uma sequência de validações. Uma regra deve poder reprovar o lote antes que as seguintes sejam executadas.

**Decisão:** cada validador estende `LoteValidationHandle`, implementa `validar` e pode encaminhar a requisição ao próximo elemento. A configuração Spring monta e publica a cadeia; `LoteIngressoService` a invoca.

**Justificativa:** permite acrescentar verificações independentes — por exemplo, elegibilidade do associado ou disponibilidade de ingressos — sem concentrar todas as regras num único método. Cada elo só precisa conhecer sua própria condição e o próximo elo.

**Exemplo — encaminhamento em `core/validation/lote/LoteValidationHandle.java`:**

```java
public final ValidationResult handle(LoteIngresso lote, ContextoCompra contexto) {
    ValidationResult result = validar(lote, contexto);
    if (!result.isValido() || proximo == null) {
        return result;
    }
    return proximo.handle(lote, contexto);
}
```

**Exemplo — elo concreto em `core/validation/lote/IntervaloLoteValidator.java`:**

```java
if (dhLocal.isBefore(dhInicio)) {
    return recusado("Início da venda do lote em " + formatar(dhInicio));
}
if (dhLocal.isAfter(dhFim)) {
    return recusado("Venda do lote fechada, jogo já aconteceu");
}
return aprovado();
```

**Configuração e chamada:** `config/LoteValidationConfig.java` liga os elos com `setProximo` e expõe o primeiro como `loteValidationChain`; `api/service/LoteIngressoService.java` chama `loteValidationChain.handle(lote, contexto)` tanto ao listar os lotes quanto, de novo, ao comprar (validação dupla no backend).

**Elos ativos, na ordem de checagem** (o motivo mostrado ao torcedor é o do primeiro elo que recusa):

| # | Elo | Regra de negócio |
|---|---|---|
| 1 | `IntervaloLoteValidator` | O pedido só vale entre o início da venda do lote e o horário do jogo. Lote sem data de início é recusado (antes causava `NullPointerException`). |
| 2 | `EstoqueLoteValidator` | O lote precisa ter ingresso disponível; a tela já mostra "esgotado". O abate atômico do estoque na compra continua existindo por causa da corrida entre compradores. |
| 3 | `VendaExclusivaValidator` | Até `dhTerminoVendaExclusiva`, só sócio compra; "não associado" espera o fim da fase ou se associa. Sem a data, o lote abre para todos. |
| 4 | `CategoriaMinimaValidator` | Se o lote tem `categoriaMinima`, o comprador precisa ter hierarquia igual ou maior. |
| 5 | `SetorPermitidoValidator` | Se o setor do estádio lista `sociosPermitidos` (ex.: camarote só Prata e Ouro), só essas categorias compram. Lista vazia = sem restrição. |
| 6 | `LimitePorJogoValidator` | No máximo N ingressos por jogo por conta (propriedade `ingresso.limite-por-jogo`, padrão 1: um ingresso por conta), somando todos os setores. Contam `RESERVADO` e `PAGO`; `CANCELADO` e `EXPIRADO` liberam a vaga. |

Os elos 3 a 6 dependem de quem compra. Para um visitante que só consulta os lotes (sem conta de torcedor), eles aprovam em vez de recusar: a compra em si já exige login. Os helpers `aprovado()`, `recusado(...)` e `compradorDe(...)` ficam em `LoteValidationHandle`.

**Como acrescentar uma regra:** crie um `@Component` que estenda `LoteValidationHandle`, implemente `validar` e ligue-o na posição certa em `LoteValidationConfig`; a regra não passa a fazer parte da cadeia só por existir como classe. Os testes da cadeia estão em `LoteValidationChainTest` (sem banco).

## Decisão: MVC com separação em camadas

**Contexto:** o sistema serve páginas web e precisa separar a recepção de requisições, as operações da aplicação, o acesso aos dados e a apresentação.

**Decisão:** usar controllers para rotas e interação com o modelo de página, services para fluxos de aplicação, repositories para persistência e templates Thymeleaf para renderização.

**Justificativa:** cada camada tem um papel identificável. Isso reduz a mistura de lógica HTTP, regras de negócio, consulta ao banco e HTML no mesmo componente, facilitando evolução localizada.

**Exemplo — controller delega ao serviço (`api/controller/JogoController.java`):**

```java
@GetMapping
public String jogosDisponiveis(Model model) {
    JogosDisponiveisDTO jogosData = jogoService.findJogosAno();
    model.addAttribute("jogosData", jogosData);
    return "jogos";
}
```

O nome `jogos` seleciona o template `src/main/resources/templates/jogos.html`, que usa atributos Thymeleaf como `th:text` e `th:each` para renderizar os dados.

**Regra de manutenção:** controllers devem tratar rotas e preparação da resposta; regras do caso de uso devem ficar nos services; acesso a dados deve ocorrer pelos repositories; templates devem cuidar da apresentação.

## Decisão: Repository para acesso a dados

**Contexto:** a aplicação precisa consultar e persistir entidades sem repetir operações comuns de CRUD e código de infraestrutura de banco.

**Decisão:** declarar interfaces de repositório que estendem `JpaRepository`, adicionando métodos derivados da nomenclatura ou consultas quando a aplicação precisa de uma operação específica.

**Justificativa:** abstrai a infraestrutura de persistência atrás de uma interface orientada às entidades. O Spring Data fornece as operações básicas e implementa métodos derivados, deixando o código da aplicação concentrado nas necessidades do domínio.

**Exemplo — `api/repository/JogoRepository.java`:**

```java
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    List<Jogo> findByDhJogoBetween(LocalDateTime dhInicio, LocalDateTime dhFim);
}
```

**Exemplo — consulta customizada em `api/repository/UsuarioRepository.java`:**

```java
Optional<Usuario> findByUsername(String username);
boolean existsByUsername(String username);
```

**Regra de manutenção:** novas consultas e persistências devem ser expressas por interfaces de repository. Não se deve duplicar consultas ou colocar SQL/JPA diretamente em controller ou template.

## Decisão: injeção de dependência e ciclo de vida Spring

**Status:** adotada pelo framework.

**Contexto:** services, factories, validadores e configurações dependem uns dos outros e de infraestrutura, como `PasswordEncoder`.

**Decisão:** registrar componentes com anotações como `@Service` e `@Component`, definir objetos de configuração com `@Bean` e receber dependências do container Spring. O escopo padrão dos beans Spring é singleton por contexto da aplicação.

**Justificativa:** deixa a criação e conexão dos componentes sob controle do container, facilita substituir implementações e evita que cada chamada construa manualmente dependências compartilhadas.

**Exemplo — construtor de `LoteService.java`:**

```java
public LoteService(@Qualifier("loteValidationChain")
                   LoteValidationHandle loteValidationChain) {
    this.loteValidationChain = loteValidationChain;
}
```

**Exemplo — `UsuarioFactory` recebe o codificador pelo construtor** (gerado por `@RequiredArgsConstructor`):

```java
@RequiredArgsConstructor
@Component
public class UsuarioFactory {
    private final PasswordEncoder passwordEncoder;
}
```

**Esclarecimento sobre Singleton:** os componentes gerenciados pelo Spring normalmente compartilham uma instância por contexto. Isso é uma configuração do ciclo de vida do framework. Não significa que `UsuarioFactory`, `LoteService` ou outras classes implementem manualmente o padrão GoF Singleton.

## Decisão existente a documentar com ressalva: `InjectionProvider`

**Status:** presente no código; funciona como um Service Locator estático e encapsula estado global.

**Contexto:** vários services obtêm repositories por métodos estáticos, em vez de receberem as dependências diretamente.

**Exemplo — `api/repository/InjectionProvider.java`:**

```java
@Getter private static UsuarioRepository usuarioRepository;
@Getter private static JogoRepository jogoRepository;

@PostConstruct
public void init() {
    InjectionProvider.usuarioRepository = usuarioRepositoryInject;
    InjectionProvider.jogoRepository = jogoRepositoryInject;
}
```

Uso em `CadastroService`:

```java
InjectionProvider.getUsuarioRepository().save(usuario);
```

**Por que essa abordagem aparece:** fornece acesso conveniente aos repositories sem adicioná-los como campos e parâmetros de cada service. Essa é a função observável da implementação; o repositório não registra a motivação histórica da equipe.

**Classificação correta:** embora o nome `InjectionProvider` e os campos estáticos possam lembrar Singleton, a classe não implementa o Singleton clássico — não tem construtor privado nem método que controle a criação da única instância. O papel exercido é mais próximo de Service Locator/global state.

**Impacto para manutenção:** o acoplamento fica menos visível nas assinaturas dos services, e testes isolados ficam mais difíceis porque os repositories são estado global. Para código novo, prefira injeção de dependência explícita por construtor, como já ocorre em `LoteService`. Se o projeto migrar os serviços existentes, atualize este registro para marcar o Service Locator como removido.

## Categorias de sócio como entidade

`CategoriaSocio` é uma entidade (nome, mensalidade, desconto de ingresso, hierarquia), em vez de um enum, para que valores e descontos sejam alterados sem mudar código. `Associado.categoria` é uma associação `@ManyToOne`. A categoria "não associado" é a linha com `hierarquia = 0` (`CategoriaSocio.isNaoAssociado()`), criada pelo `DataSeeder`; `UsuarioFactory` a busca ao criar um usuário. Mudanças de categoria passam por `SocioTorcedorService`, que atualiza `dataAdesao` e grava `HistoricoCategoria`.

## Decisão: interface com implementação de demonstração para recursos sem backend

**Contexto:** o front novo tem telas que dependem de dados que o domínio ainda não tem, como notícias do clube e o programa de pontos do sócio. Criar entidades ou tabelas só para isso mudaria o banco, e deixar as telas de fora limitaria o produto ao que existe hoje.

**Decisão:** cada recurso desses é uma **interface** em `api/service` (`NoticiaService`, `FidelidadeService`). A implementação atual fica em `api/service/mock/` (`NoticiaMockService`, `FidelidadeMockService`) e devolve DTOs fixos em memória. Os controllers recebem a interface pelo construtor e não conhecem a implementação.

**Justificativa:** quando o backend implementar o recurso de verdade, basta criar outra classe que implemente a mesma interface e retirar a de demonstração. Controller e template continuam iguais. O mesmo princípio de depender de abstrações já aparece no projeto com `LoteValidationHandle`, em que o serviço conhece só a classe base da cadeia.

**Exemplo — `api/controller/NoticiaController.java`:**

```java
@Controller
@RequestMapping("/noticias")
@RequiredArgsConstructor
public class NoticiaController {
    private final NoticiaService noticiaService; // interface; hoje o Spring injeta NoticiaMockService

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("noticias", noticiaService.listar());
        return "noticias/lista";
    }
}
```

**Limite e regras de manutenção:** as implementações de demonstração não gravam nada e não criam entidades. Cada uma tem no topo um comentário dizendo o que simula e o que falta no backend. Na tela, um dado de demonstração que pareça real (como um saldo de pontos) vem marcado como prévia, e ações sem backend ficam desabilitadas. O roteiro de cada recurso está em `docs/front/dados-ficticios.md`; ao implementar um deles de verdade, atualize esse arquivo e esta seção.

## Regras para manter código e documento alinhados

1. Ao criar um usuário, use `UsuarioFactory` para preservar a montagem e os valores iniciais num único ponto.
2. Ao expor entidades como dados de página, use ou amplie `DTOFactory`; evite remontar os mesmos DTOs em vários services.
3. Ao acrescentar regras de disponibilidade de lote, implemente cada condição como elo e conecte os elos na configuração da cadeia.
4. Preserve a separação entre controllers, services, repositories e templates.
5. Para novas dependências de services e componentes, prefira injeção por construtor; não amplie o uso de acessos estáticos de `InjectionProvider`.
6. Se a implementação mudar de modo que qualquer decisão acima deixe de ser verdadeira, atualize este documento na mesma alteração.
7. Recurso de tela sem backend entra por uma interface em `api/service` com implementação em `api/service/mock/`, e é registrado em `docs/front/dados-ficticios.md`.
