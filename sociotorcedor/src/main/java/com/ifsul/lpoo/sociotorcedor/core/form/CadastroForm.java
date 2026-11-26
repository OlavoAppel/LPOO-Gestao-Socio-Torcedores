package com.ifsul.lpoo.sociotorcedor.core.form;

import com.ifsul.lpoo.sociotorcedor.core.validation.cadastro.Cpf;
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

    // Sem espaços: o login não trata espaços, então uma conta assim nunca conseguiria entrar.
    @NotBlank(message = "Informe o usuário.")
    // O "?" externo deixa o campo vazio por conta do @NotBlank, para não mostrar duas mensagens.
    @Pattern(regexp = "([A-Za-z0-9._-]{3,30})?",
            message = "Use de 3 a 30 caracteres: letras, números, ponto, hífen ou sublinhado, sem espaços.")
    private String username;

    @NotBlank(message = "Informe a senha.")
    private String senha;

    @NotBlank(message = "Informe o e-mail.")
    // O @Email padrão aceita "a@b"; por isso exigimos também um domínio com ponto (ex.: nome@email.com).
    @Pattern(regexp = "([^\\s@]+@[^\\s@]+\\.[^\\s@.]+)?", message = "Informe um e-mail válido, como nome@email.com.")
    private String email;

    @NotBlank(message = "Informe o nome.")
    private String nome;

    @NotBlank(message = "Informe o CPF.")
    @Cpf
    private String cpf;

    private String telefone;
    private LocalDate dataNascimento;

    // Os setters abaixo removem espaços nas pontas (a senha não é alterada: espaços podem fazer parte dela).
    public void setUsername(String username) {
        this.username = trim(username);
    }

    public void setEmail(String email) {
        this.email = trim(email);
    }

    public void setNome(String nome) {
        this.nome = trim(nome);
    }

    public void setCpf(String cpf) {
        this.cpf = trim(cpf);
    }

    private static String trim(String valor) {
        return valor == null ? null : valor.trim();
    }

}
