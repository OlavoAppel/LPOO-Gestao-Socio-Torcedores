package com.ifsul.lpoo.sociotorcedor.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;

/**
 * Formatação usada só pelos templates ({@code ${@formato.real(valor)}}).
 *
 * <p>Fixa o português do Brasil para que datas e valores saiam iguais em qualquer navegador,
 * independentemente do idioma que ele envia. Não contém regra de negócio: só transforma
 * números e datas em texto.
 */
@Component("formato")
public class FormatadorView {

    private static final Locale PT_BR = Locale.of("pt", "BR");
    private static final DateTimeFormatter DIA_SEMANA = DateTimeFormatter.ofPattern("EEE", PT_BR);
    private static final DateTimeFormatter DIA_SEMANA_LONGO = DateTimeFormatter.ofPattern("EEEE", PT_BR);
    private static final DateTimeFormatter MES_CURTO = DateTimeFormatter.ofPattern("MMM", PT_BR);
    private static final DateTimeFormatter MES_LONGO = DateTimeFormatter.ofPattern("MMMM", PT_BR);
    private static final DateTimeFormatter NUMERICA = DateTimeFormatter.ofPattern("dd/MM/yyyy", PT_BR);
    private static final DateTimeFormatter DIA_MES = DateTimeFormatter.ofPattern("dd/MM", PT_BR);
    private static final DateTimeFormatter MES_ANO = DateTimeFormatter.ofPattern("MM/yyyy", PT_BR);
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /** "R$ 1.234,50"; nulo vira "R$ 0,00". */
    public String real(BigDecimal valor) {
        return "R$ " + decimal(valor);
    }

    /** "1.234,50", sem o símbolo da moeda. */
    public String decimal(BigDecimal valor) {
        DecimalFormat f = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT_BR));
        return f.format(valor == null ? BigDecimal.ZERO : valor.setScale(2, RoundingMode.HALF_UP));
    }

    /** "1.234". */
    public String inteiro(Number valor) {
        DecimalFormat f = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(PT_BR));
        return f.format(valor == null ? 0 : valor);
    }

    /** "38,5%" (uma casa) ou "40%" quando inteiro. */
    public String porcentagem(BigDecimal valor) {
        BigDecimal v = valor == null ? BigDecimal.ZERO : valor;
        String padrao = v.stripTrailingZeros().scale() <= 0 ? "#,##0" : "#,##0.0";
        return new DecimalFormat(padrao, DecimalFormatSymbols.getInstance(PT_BR)).format(v) + "%";
    }

    /** Porcentagem de parte sobre total, com uma casa ("62,5%"). Total zero vira "0%". */
    public String fracao(Number parte, Number total) {
        if (parte == null || total == null || total.doubleValue() == 0) {
            return "0%";
        }
        BigDecimal p = BigDecimal.valueOf(parte.doubleValue() * 100 / total.doubleValue()).setScale(1, RoundingMode.HALF_UP);
        return porcentagem(p);
    }

    /**
     * Largura de barra em CSS: parte / total * 100, entre 0 e 100, com ponto decimal ("38.5").
     * Total zero ou nulo vira "0" (sem divisão por zero).
     */
    public String proporcao(Number parte, Number total) {
        if (parte == null || total == null || total.doubleValue() <= 0) {
            return "0";
        }
        double p = Math.max(0, Math.min(100, parte.doubleValue() * 100 / total.doubleValue()));
        return BigDecimal.valueOf(p).setScale(1, RoundingMode.HALF_UP).toPlainString();
    }

    /** Maior valor de uma lista de números (zero se vazia), para a escala das barras. */
    public BigDecimal maximo(java.util.List<? extends Number> valores) {
        if (valores == null) {
            return BigDecimal.ZERO;
        }
        return valores.stream()
                .filter(java.util.Objects::nonNull)
                .map(v -> new BigDecimal(v.toString()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    /** "sáb" */
    public String diaSemana(Temporal data) {
        return data == null ? "" : semPonto(DIA_SEMANA.format(data));
    }

    /** "Sábado" */
    public String diaSemanaLongo(Temporal data) {
        return data == null ? "" : maiuscula(DIA_SEMANA_LONGO.format(data));
    }

    /** "23 nov" */
    public String diaMes(Temporal data) {
        if (data == null) return "";
        return dia(data) + " " + semPonto(MES_CURTO.format(data));
    }

    /** "Sáb, 23 nov" */
    public String dataCurta(Temporal data) {
        if (data == null) return "";
        return maiuscula(diaSemana(data)) + ", " + diaMes(data);
    }

    /** "sábado, 23 de novembro" */
    public String dataLonga(Temporal data) {
        if (data == null) return "";
        return DIA_SEMANA_LONGO.format(data) + ", " + dia(data) + " de " + MES_LONGO.format(data);
    }

    /** "23/11/2026" */
    public String dataNumerica(Temporal data) {
        return data == null ? "" : NUMERICA.format(data);
    }

    /** "23/11" */
    public String diaMesNumerico(Temporal data) {
        return data == null ? "" : DIA_MES.format(data);
    }

    /** "03/2026" */
    public String mesAno(Temporal data) {
        return data == null ? "" : MES_ANO.format(data);
    }

    /** "Novembro" a partir do número do mês (1 a 12). */
    public String nomeMes(int mes) {
        return maiuscula(MES_LONGO.format(LocalDate.of(2000, mes, 1)));
    }

    /** "21h" ou "18h30". */
    public String hora(LocalDateTime data) {
        if (data == null) return "";
        return data.getMinute() == 0
                ? data.getHour() + "h"
                : data.getHour() + "h" + String.format("%02d", data.getMinute());
    }

    /** Valor para o atributo datetime de &lt;time&gt;. */
    public String iso(LocalDateTime data) {
        return data == null ? "" : ISO.format(data);
    }

    /** Valor para o atributo datetime de &lt;time&gt; com data sem hora. */
    public String isoData(LocalDate data) {
        return data == null ? "" : data.toString();
    }

    /** Primeiro nome de "Ana Silva" é "Ana". */
    public String primeiroNome(String nome) {
        if (nome == null || nome.isBlank()) return "";
        return nome.trim().split("\\s+")[0];
    }

    /** Primeiros 8 caracteres do código do ingresso, em maiúsculas (o que vai no estádio). */
    public String codigoCurto(String codigo) {
        if (codigo == null) return "";
        String limpo = codigo.replace("-", "");
        return limpo.substring(0, Math.min(8, limpo.length())).toUpperCase(PT_BR);
    }

    /** Número de sócio com 6 dígitos: 14 vira "000014". */
    public String numeroSocio(Long id) {
        return id == null ? "" : String.format("%06d", id);
    }

    private String dia(Temporal data) {
        return String.valueOf(LocalDate.from(data).getDayOfMonth());
    }

    private static String semPonto(String texto) {
        return texto.endsWith(".") ? texto.substring(0, texto.length() - 1) : texto;
    }

    private static String maiuscula(String texto) {
        return texto.isEmpty() ? texto : texto.substring(0, 1).toUpperCase(PT_BR) + texto.substring(1);
    }
}
