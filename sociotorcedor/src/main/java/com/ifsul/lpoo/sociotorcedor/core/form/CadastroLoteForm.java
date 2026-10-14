package com.ifsul.lpoo.sociotorcedor.core.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

}
