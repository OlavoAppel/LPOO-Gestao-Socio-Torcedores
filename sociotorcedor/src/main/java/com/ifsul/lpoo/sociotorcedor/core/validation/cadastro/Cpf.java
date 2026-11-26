package com.ifsul.lpoo.sociotorcedor.core.validation.cadastro;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** CPF com exatamente 11 números e dígitos verificadores corretos. Campo vazio é tratado por @NotBlank. */
@Documented
@Constraint(validatedBy = CpfValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cpf {

    String message() default "CPF inválido. Informe os 11 números, sem pontos nem traço.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
