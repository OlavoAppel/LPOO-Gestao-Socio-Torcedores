package com.ifsul.lpoo.sociotorcedor.core.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ValidationResult {
    private boolean valido;
    private String motivo;

    public ValidationResult(String motivo){
        this.valido = motivo.isEmpty();
        this.motivo = motivo;
    }
}
