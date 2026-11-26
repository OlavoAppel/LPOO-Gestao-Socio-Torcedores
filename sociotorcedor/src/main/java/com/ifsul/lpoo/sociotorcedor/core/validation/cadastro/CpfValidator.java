package com.ifsul.lpoo.sociotorcedor.core.validation.cadastro;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isEmpty()) {
            return true; // obrigatoriedade é responsabilidade do @NotBlank
        }
        if (!cpf.matches("\\d{11}")) {
            return false;
        }
        if (cpf.chars().distinct().count() == 1) {
            return false; // 111.111.111-11 etc. passam na conta, mas não são CPFs reais
        }
        return digito(cpf, 9) == cpf.charAt(9) - '0'
                && digito(cpf, 10) == cpf.charAt(10) - '0';
    }

    /** Dígito verificador calculado sobre os {@code quantidade} primeiros números (pesos de quantidade+1 até 2). */
    private int digito(String cpf, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (cpf.charAt(i) - '0') * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
