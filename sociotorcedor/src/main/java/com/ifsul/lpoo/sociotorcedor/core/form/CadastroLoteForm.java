package com.ifsul.lpoo.sociotorcedor.core.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CadastroLoteForm {

    @NotNull(message = "Selecione o setor")
    private Long setorId;

    @NotNull(message = "Informe o preço")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal precoBase;

    @NotNull(message = "Informe o estoque")
    @Min(value = 1, message = "O estoque deve ser de pelo menos 1")
    private Integer estoque;

    // O formato é o do <input type="datetime-local">, o mesmo do formulário de jogo.
    @NotNull(message = "Informe quando a venda começa")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dhInicioVenda;

    /** Opcional: até quando a venda é exclusiva (depois disso o lote abre para todos). */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dhTerminoVendaExclusiva;

}
