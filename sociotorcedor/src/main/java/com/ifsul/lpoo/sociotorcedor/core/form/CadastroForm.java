package com.ifsul.lpoo.sociotorcedor.core.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CadastroForm {

    @NotBlank
    private String username;

    @NotBlank
    private String senha;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String nome;

    @Pattern(regexp = "\\d{11}")
    private String cpf;

    private String telefone;
    private LocalDate dataNascimento;


}
