package com.ifsul.lpoo.sociotorcedor.migration;

import com.ifsul.lpoo.sociotorcedor.api.repository.*;
import com.ifsul.lpoo.sociotorcedor.core.factory.UsuarioFactory;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.*;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.StatusIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Carga completa de dados fictícios para desenvolvimento e demonstração.
 *
 * Só executa em banco vazio (nenhum usuário cadastrado). Para repovoar, recrie o banco.
 * A carga é determinística (Random com semente fixa) e roda em uma única transação:
 * se algo falhar, nada é gravado pela metade.
 *
 * Acessos: admin / admin (ADMIN) e qualquer usuário gerado com a senha {@value #SENHA_TORCEDORES}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final String SENHA_TORCEDORES = "123456";
    private static final int QUANTIDADE_TORCEDORES = 48;

    private final UsuarioRepository usuarioRepository;
    private final CategoriaSocioRepository categoriaSocioRepository;
    private final BeneficioSocioRepository beneficioSocioRepository;
    private final HistoricoCategoriaRepository historicoCategoriaRepository;
    private final TimeRepository timeRepository;
    private final EstadioRepository estadioRepository;
    private final JogoRepository jogoRepository;
    private final IngressoRepository ingressoRepository;
    private final UsuarioFactory usuarioFactory;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random(42);
    private final LocalDateTime agora = LocalDateTime.now();

    // ------------------------------------------------------------------ dados-base

    /** nome, sigla. O escudo de cada time é /images/escudos/{sigla em minúsculas}.png */
    private static final String[][] TIMES = {
            // Brasileirão Série A 2026
            {"Athletico Paranaense", "CAP"}, {"Atlético Mineiro", "CAM"}, {"Bahia", "BAH"},
            {"Botafogo", "BOT"}, {"Chapecoense", "CHA"}, {"Corinthians", "COR"}, {"Coritiba", "CFC"},
            {"Cruzeiro", "CRU"}, {"Flamengo", "FLA"}, {"Fluminense", "FLU"}, {"Grêmio", "GRE"},
            {"Internacional", "INT"}, {"Mirassol", "MIR"}, {"Palmeiras", "PAL"},
            {"Red Bull Bragantino", "RBB"}, {"Remo", "REM"}, {"Santos", "SAN"}, {"São Paulo FC", "SPFC"},
            {"Vasco da Gama", "VAS"}, {"Vitória", "VIT"},
            // Libertadores 2026 (fase de grupos), exceto os brasileiros acima
            {"Always Ready", "ARE"}, {"Barcelona SC", "BSC"}, {"Boca Juniors", "CABJ"}, {"Bolívar", "BOL"},
            {"Cerro Porteño", "CER"}, {"Coquimbo Unido", "COQ"}, {"Cusco FC", "CUS"},
            {"Deportes Tolima", "TOL"}, {"Deportivo La Guaira", "DLG"}, {"Estudiantes de La Plata", "EST"},
            {"Independiente del Valle", "IDV"}, {"Independiente Medellín", "DIM"},
            {"Independiente Rivadavia", "RIV"}, {"Junior", "JUN"}, {"Lanús", "LAN"}, {"LDU Quito", "LDU"},
            {"Libertad", "LIB"}, {"Nacional", "NAC"}, {"Peñarol", "PEN"}, {"Platense", "PLA"},
            {"Rosario Central", "ROS"}, {"Santa Fe", "ISF"}, {"Sporting Cristal", "SCR"},
            {"Universidad Católica", "UCA"}, {"Universidad Central", "UCV"}, {"Universitario", "UNI"}
    };

    private static final String[] ESTADIOS = {
            "MorumBIS", "Maracanã", "Beira-Rio", "Arena do Grêmio", "Mineirão",
            "Allianz Parque", "Neo Química Arena", "La Bombonera"
    };

    /** Setores iguais em todos os estádios: nome, capacidade, preço-base do lote. */
    private static final Object[][] SETORES = {
            {"Arquibancada Popular", 8000, "40.00"},
            {"Cadeira Inferior", 3000, "90.00"},
            {"Cadeira Superior", 2500, "70.00"},
            {"Camarote", 300, "250.00"}
    };
    /** Quantidade de ingressos de cada lote (na mesma ordem de SETORES) e peso na escolha do comprador. */
    private static final int[] INGRESSOS_POR_LOTE = {40, 30, 30, 10};
    private static final int[] PESO_COMPRA = {5, 3, 3, 1};

    /** campeonato, estadio, casa, fora, dias a partir de hoje, hora, minuto */
    private static final Object[][] JOGOS = {
            {"Brasileirão 2026", "MorumBIS", "SPFC", "GRE", -150, 16, 0},
            {"Copa Libertadores 2026", "La Bombonera", "CABJ", "PAL", -135, 21, 30},
            {"Brasileirão 2026", "Maracanã", "FLA", "INT", -110, 18, 30},
            {"Brasileirão 2026", "Allianz Parque", "PAL", "COR", -95, 19, 0},
            {"Copa Libertadores 2026", "Maracanã", "FLU", "LDU", -80, 21, 30},
            {"Brasileirão 2026", "Beira-Rio", "INT", "GRE", -62, 16, 0},
            {"Brasileirão 2026", "Mineirão", "CAM", "CRU", -45, 18, 30},
            {"Brasileirão 2026", "MorumBIS", "SPFC", "CAM", -30, 21, 30},
            {"Brasileirão 2026", "Arena do Grêmio", "GRE", "FLA", -18, 19, 0},
            {"Copa Libertadores 2026", "La Bombonera", "CABJ", "FLU", -8, 21, 30},
            {"Brasileirão 2026", "MorumBIS", "SPFC", "GRE", 3, 21, 0},
            {"Brasileirão 2026", "Neo Química Arena", "COR", "PAL", 9, 16, 0},
            {"Brasileirão 2026", "Beira-Rio", "INT", "FLA", 20, 18, 30},
            {"Brasileirão 2026", "Maracanã", "FLU", "VAS", 32, 16, 0}
    };

    private static final String[] NOMES = {
            "Ana", "Bruno", "Carla", "Diego", "Eduarda", "Felipe", "Gabriela", "Henrique", "Isabela", "João",
            "Karina", "Lucas", "Mariana", "Nicolas", "Olivia", "Pedro", "Rafaela", "Samuel", "Tatiane", "Victor",
            "Amanda", "Caio", "Débora", "Eduardo", "Fernanda", "Gustavo", "Helena", "Igor", "Juliana", "Leonardo"
    };
    private static final String[] SOBRENOMES = {
            "Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes",
            "Costa", "Ribeiro", "Martins", "Carvalho", "Almeida", "Lopes", "Soares", "Fernandes", "Vieira", "Barbosa",
            "Rocha", "Dias", "Nunes", "Moreira", "Cardoso", "Teixeira", "Araújo", "Machado", "Freitas", "Mendes"
    };

    // ------------------------------------------------------------------ fluxo principal

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("DataSeeder: banco já possui dados, carga ignorada.");
            return;
        }

        Categorias categorias = criarCategorias();
        criarBeneficios(categorias);
        criarAdmin();
        List<Usuario> torcedores = criarTorcedores(categorias);

        Map<String, Time> times = criarTimes();
        Map<String, Estadio> estadios = criarEstadios(categorias);
        List<Jogo> jogos = criarJogos(times, estadios, categorias);
        int ingressos = criarIngressos(jogos, torcedores, categorias);

        log.info("DataSeeder: carga concluída ({} categorias, {} usuários, {} times, {} estádios, {} jogos, {} ingressos).",
                4, torcedores.size() + 1, times.size(), estadios.size(), jogos.size(), ingressos);
    }

    // ------------------------------------------------------------------ categorias e benefícios

    private record Categorias(CategoriaSocio naoAssociado, CategoriaSocio bronze, CategoriaSocio prata, CategoriaSocio ouro) {
        List<CategoriaSocio> todas() {
            return List.of(naoAssociado, bronze, prata, ouro);
        }
    }

    private Categorias criarCategorias() {
        // descontoIngresso é uma porcentagem (10 = 10%)
        CategoriaSocio naoAssociado = criarCategoria("Não associado", 0, "0.00", "0.00");
        CategoriaSocio bronze = criarCategoria("Sócio Bronze", 1, "29.90", "10.00");
        CategoriaSocio prata = criarCategoria("Sócio Prata", 2, "59.90", "25.00");
        CategoriaSocio ouro = criarCategoria("Sócio Ouro", 3, "119.90", "40.00");
        categoriaSocioRepository.saveAll(List.of(naoAssociado, bronze, prata, ouro));
        return new Categorias(naoAssociado, bronze, prata, ouro);
    }

    private CategoriaSocio criarCategoria(String nome, int hierarquia, String mensalidade, String descontoIngresso) {
        CategoriaSocio categoria = new CategoriaSocio();
        categoria.setNome(nome);
        categoria.setHierarquia(hierarquia);
        categoria.setMensalidade(new BigDecimal(mensalidade));
        categoria.setDescontoIngresso(new BigDecimal(descontoIngresso));
        return categoria;
    }

    private void criarBeneficios(Categorias c) {
        beneficioSocioRepository.saveAll(List.of(
                beneficio("Desconto em ingressos", "10% de desconto na compra de ingressos.", "10.00", "0.00", c.bronze()),
                beneficio("Brinde de boas-vindas", "Copo oficial do clube na adesão.", "0.00", "100.00", c.bronze()),
                beneficio("Desconto em ingressos", "25% de desconto na compra de ingressos.", "25.00", "0.00", c.prata()),
                beneficio("Camiseta do sócio", "Camiseta exclusiva liberada para retirada no clube.", "0.00", "100.00", c.prata()),
                beneficio("Desconto em ingressos", "40% de desconto na compra de ingressos.", "40.00", "0.00", c.ouro()),
                beneficio("Visita guiada ao clube", "Tour pelos bastidores e museu, uma vez por temporada.", "0.00", "0.00", c.ouro()),
                beneficio("Brindes premium", "Kit de brindes com 30% de desconto na loja oficial.", "0.00", "30.00", c.ouro())
        ));
    }

    private BeneficioSocio beneficio(String nome, String descricao, String descontoIngresso, String descontoBrinde,
                                     CategoriaSocio categoria) {
        BeneficioSocio b = new BeneficioSocio();
        b.setNome(nome);
        b.setDescricao(descricao);
        b.setDescontoIngresso(new BigDecimal(descontoIngresso));
        b.setDescontoBrindes(new BigDecimal(descontoBrinde));
        b.setCategoria(categoria);
        return b;
    }

    // ------------------------------------------------------------------ usuários, sócios e histórico

    private void criarAdmin() {
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@sociotorcedor.com");
        admin.setRole("ADMIN");
        usuarioRepository.save(admin);
    }

    private List<Usuario> criarTorcedores(Categorias c) {
        List<Usuario> usuarios = new ArrayList<>();
        List<HistoricoCategoria> historicos = new ArrayList<>();
        Set<String> usernames = new HashSet<>();

        for (int i = 0; i < QUANTIDADE_TORCEDORES; i++) {
            String nome = NOMES[i % NOMES.length];
            String sobrenome = SOBRENOMES[(i * 7 + (i / NOMES.length) * 11 + 3) % SOBRENOMES.length];

            String username = semAcento(nome + "." + sobrenome).toLowerCase();
            if (!usernames.add(username)) {
                username = username + i;
                usernames.add(username);
            }

            CadastroForm form = new CadastroForm();
            form.setNome(nome + " " + sobrenome);
            form.setUsername(username);
            form.setEmail(username + "@email.com");
            form.setSenha(SENHA_TORCEDORES);
            form.setCpf(gerarCpf(i));
            form.setTelefone("5399" + String.format("%07d", 1000000 + i * 7919 % 8999999));
            form.setDataNascimento(LocalDate.of(1960 + random.nextInt(46), 1 + random.nextInt(12), 1 + random.nextInt(28)));

            Usuario usuario = usuarioFactory.createNewUsuario(form); // nasce como "não associado"
            definirPlano(usuario.getAssociado(), c, historicos);
            usuarios.add(usuario);
        }

        usuarioRepository.saveAll(usuarios);
        // o histórico referencia os associados, por isso só é gravado depois deles
        historicoCategoriaRepository.saveAll(historicos);
        return usuarios;
    }

    /**
     * Sorteia a situação do torcedor: ~35% não associados; os demais se associam em uma data dos últimos
     * 6 meses (parte deles neste mês). Alguns fazem upgrade de Bronze para Prata e alguns cancelam depois.
     */
    private void definirPlano(Associado associado, Categorias c, List<HistoricoCategoria> historicos) {
        double sorteio = random.nextDouble();
        if (sorteio < 0.35) {
            return;
        }

        CategoriaSocio inicial = sorteio < 0.75 ? c.bronze() : (sorteio < 0.93 ? c.prata() : c.ouro());
        LocalDate hoje = agora.toLocalDate();
        LocalDate adesao = random.nextDouble() < 0.25
                ? hoje.minusDays(random.nextInt(Math.max(1, hoje.getDayOfMonth())))   // neste mês
                : hoje.minusDays(1 + random.nextInt(180));                             // últimos 6 meses

        associado.setCategoria(inicial);
        associado.setDataAdesao(adesao);
        historicos.add(historico(associado, c.naoAssociado(), inicial, adesao));

        long diasDesdeAdesao = ChronoUnit.DAYS.between(adesao, hoje);
        if (diasDesdeAdesao < 2) {
            return;
        }
        LocalDate depois = adesao.plusDays(1 + random.nextInt((int) diasDesdeAdesao - 1));

        if (inicial == c.bronze() && random.nextDouble() < 0.25) {
            associado.setCategoria(c.prata());
            historicos.add(historico(associado, c.bronze(), c.prata(), depois));
        } else if (random.nextDouble() < 0.06) {
            associado.setCategoria(c.naoAssociado());
            associado.setDataAdesao(null);
            historicos.add(historico(associado, inicial, c.naoAssociado(), depois));
        }
    }

    private HistoricoCategoria historico(Associado associado, CategoriaSocio anterior, CategoriaSocio nova, LocalDate data) {
        HistoricoCategoria h = new HistoricoCategoria();
        h.setAssociado(associado);
        h.setCategoriaAnterior(anterior);
        h.setCategoriaNova(nova);
        h.setData(data);
        return h;
    }

    // ------------------------------------------------------------------ times, estádios, jogos

    private Map<String, Time> criarTimes() {
        Map<String, Time> porSigla = new LinkedHashMap<>();
        for (String[] t : TIMES) {
            Time time = new Time();
            time.setNome(t[0]);
            time.setSigla(t[1]);
            time.setDivisao(1);
            time.setEscudoPath("/images/escudos/" + t[1].toLowerCase() + ".png");
            porSigla.put(t[1], time);
        }
        timeRepository.saveAll(porSigla.values());
        return porSigla;
    }

    private Map<String, Estadio> criarEstadios(Categorias c) {
        Map<String, Estadio> porNome = new LinkedHashMap<>();
        for (String nome : ESTADIOS) {
            Estadio estadio = new Estadio();
            estadio.setNome(nome);

            List<SetorEstadio> setores = new ArrayList<>();
            for (Object[] s : SETORES) {
                SetorEstadio setor = new SetorEstadio();
                setor.setNome((String) s[0]);
                setor.setCapacidadeTotal((Integer) s[1]);
                setor.setEstadio(estadio);
                // o camarote é exclusivo de Prata e Ouro; os demais setores aceitam todas as categorias
                setor.setSociosPermitidos("Camarote".equals(s[0])
                        ? List.of(c.prata(), c.ouro())
                        : c.todas());
                setores.add(setor);
            }
            estadio.setSetores(setores);
            porNome.put(nome, estadio);
        }
        estadioRepository.saveAll(porNome.values()); // os setores são gravados em cascata
        return porNome;
    }

    private List<Jogo> criarJogos(Map<String, Time> times, Map<String, Estadio> estadios, Categorias c) {
        List<Jogo> jogos = new ArrayList<>();
        for (Object[] j : JOGOS) {
            Jogo jogo = new Jogo();
            jogo.setCampeonato((String) j[0]);
            jogo.setEstadio(estadios.get((String) j[1]));
            jogo.setCasa(times.get((String) j[2]));
            jogo.setFora(times.get((String) j[3]));
            jogo.setDhJogo(agora.toLocalDate().plusDays((Integer) j[4]).atTime((Integer) j[5], (Integer) j[6]));

            List<LoteIngresso> lotes = new ArrayList<>();
            List<SetorEstadio> setores = jogo.getEstadio().getSetores();
            for (int s = 0; s < setores.size(); s++) {
                lotes.add(criarLote(jogo, setores.get(s), s, c));
            }
            jogo.setLoteIngressoList(lotes);
            jogos.add(jogo);
        }
        // os jogos (e seus lotes, em cascata) são gravados em criarIngressos, depois de calcular o estoque
        return jogos;
    }

    private LoteIngresso criarLote(Jogo jogo, SetorEstadio setor, int indiceSetor, Categorias c) {
        LoteIngresso lote = new LoteIngresso();
        lote.setJogo(jogo);
        lote.setSetor(setor);
        lote.setNome(setor.getNome());
        lote.setPrecoBase(new BigDecimal((String) SETORES[indiceSetor][2]));
        lote.setIngressosTotaisLote(INGRESSOS_POR_LOTE[indiceSetor]);
        lote.setIngressoDisponiveis(INGRESSOS_POR_LOTE[indiceSetor]);
        lote.setDhInicioVenda(jogo.getDhJogo().minusDays(14));
        lote.setDhTerminoVendaExclusiva(jogo.getDhJogo().minusDays(10));
        lote.setCategoriaMinima("Camarote".equals(setor.getNome()) ? c.prata() : null);
        return lote;
    }

    // ------------------------------------------------------------------ ingressos

    /**
     * Gera as compras de cada torcedor, respeitando janela de venda, estoque do lote e categoria mínima.
     * Retorna a quantidade de ingressos criados. Também grava os jogos e seus lotes.
     */
    private int criarIngressos(List<Jogo> jogos, List<Usuario> torcedores, Categorias c) {
        List<Ingresso> ingressos = new ArrayList<>();
        // IdentityHashMap: as entidades têm equals/hashCode do Lombok (@Data) com relacionamentos circulares
        Map<LoteIngresso, Integer> vendidosPorLote = new IdentityHashMap<>();

        for (Jogo jogo : jogos) {
            boolean jogoPassado = jogo.getDhJogo().isBefore(agora);
            double probabilidade = jogoPassado ? 0.55 : 0.30;

            for (Usuario usuario : torcedores) {
                if (random.nextDouble() >= probabilidade) {
                    continue;
                }
                LoteIngresso lote = escolherLote(jogo, usuario.getAssociado(), vendidosPorLote);
                if (lote == null) {
                    continue; // vendas ainda fechadas, lote esgotado ou sem lote elegível
                }
                Ingresso ingresso = criarIngresso(jogo, lote, usuario.getAssociado(), jogoPassado);
                ingressos.add(ingresso);

                if (ingresso.getStatus() == StatusIngresso.PAGO || ingresso.getStatus() == StatusIngresso.RESERVADO) {
                    vendidosPorLote.merge(lote, 1, Integer::sum);
                }
            }
        }

        // estoque coerente com as vendas
        vendidosPorLote.forEach((lote, vendidos) ->
                lote.setIngressoDisponiveis(lote.getIngressosTotaisLote() - vendidos));

        jogoRepository.saveAll(jogos);          // grava os lotes em cascata
        ingressoRepository.saveAll(ingressos);  // agora os lotes já têm id
        return ingressos.size();
    }

    private LoteIngresso escolherLote(Jogo jogo, Associado comprador, Map<LoteIngresso, Integer> vendidosPorLote) {
        List<LoteIngresso> candidatos = new ArrayList<>();
        List<Integer> pesos = new ArrayList<>();
        List<LoteIngresso> lotes = jogo.getLoteIngressoList();

        for (int i = 0; i < lotes.size(); i++) {
            LoteIngresso lote = lotes.get(i);
            boolean vendaAberta = lote.getDhInicioVenda().isBefore(agora);
            boolean esgotado = vendidosPorLote.getOrDefault(lote, 0) >= lote.getIngressosTotaisLote();
            boolean elegivel = lote.getCategoriaMinima() == null
                    || (comprador.getCategoria().getHierarquia() >= lote.getCategoriaMinima().getHierarquia());
            if (vendaAberta && !esgotado && elegivel) {
                candidatos.add(lote);
                pesos.add(PESO_COMPRA[i]);
            }
        }
        if (candidatos.isEmpty()) {
            return null;
        }

        int sorteio = random.nextInt(pesos.stream().mapToInt(Integer::intValue).sum());
        for (int i = 0; i < candidatos.size(); i++) {
            sorteio -= pesos.get(i);
            if (sorteio < 0) {
                return candidatos.get(i);
            }
        }
        return candidatos.get(0);
    }

    private Ingresso criarIngresso(Jogo jogo, LoteIngresso lote, Associado comprador, boolean jogoPassado) {
        LocalDateTime inicio = lote.getDhInicioVenda();
        LocalDateTime fim = jogo.getDhJogo().minusHours(1).isBefore(agora.minusMinutes(5))
                ? jogo.getDhJogo().minusHours(1)
                : agora.minusMinutes(5);
        LocalDateTime dhCompra = inicio.plusSeconds((long) (random.nextDouble() * Math.max(1, Duration.between(inicio, fim).getSeconds())));

        // o desconto só vale se já era sócio na data da compra
        boolean socioNaCompra = comprador.getDataAdesao() != null
                && !dhCompra.toLocalDate().isBefore(comprador.getDataAdesao());
        BigDecimal preco = socioNaCompra
                ? comprador.getCategoria().aplicarDescontoIngresso(lote.getPrecoBase())
                : lote.getPrecoBase().setScale(2);

        Ingresso ingresso = new Ingresso();
        ingresso.setAssociado(comprador);
        ingresso.setLote(lote);
        ingresso.setPrecoPago(preco);
        ingresso.setDhCompra(dhCompra);
        ingresso.setCodigo(UUID.randomUUID().toString());
        ingresso.setStatus(sortearStatus(jogoPassado));
        return ingresso;
    }

    /** ~85% pagos; o restante cancelado/expirado, e reservas apenas em jogos que ainda vão acontecer. */
    private StatusIngresso sortearStatus(boolean jogoPassado) {
        double s = random.nextDouble();
        if (s < 0.85) return StatusIngresso.PAGO;
        if (s < 0.92) return StatusIngresso.CANCELADO;
        if (s < 0.97 || jogoPassado) return StatusIngresso.EXPIRADO;
        return StatusIngresso.RESERVADO;
    }

    // ------------------------------------------------------------------ utilitários

    private String semAcento(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    /** CPF com dígitos verificadores válidos, derivado do índice para ser único e reproduzível. */
    private String gerarCpf(int indice) {
        int[] d = new int[11];
        long base = 100_000_000L + (indice * 7_777_777L) % 899_999_999L;
        String nove = String.valueOf(base);
        for (int i = 0; i < 9; i++) {
            d[i] = nove.charAt(i) - '0';
        }
        d[9] = digitoCpf(d, 9);
        d[10] = digitoCpf(d, 10);

        StringBuilder cpf = new StringBuilder();
        for (int digito : d) {
            cpf.append(digito);
        }
        return cpf.toString();
    }

    private int digitoCpf(int[] d, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += d[i] * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

}
