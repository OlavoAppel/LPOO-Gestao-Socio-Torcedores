package com.ifsul.lpoo.sociotorcedor.core.exception;

public class CadastroException extends RuntimeException {

    /** Campo do formulário a que o erro se refere; nulo quando é um erro geral. */
    private final String campo;

    public CadastroException(String message) {
        this(null, message);
    }

    public CadastroException(String campo, String message) {
        super(message);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
