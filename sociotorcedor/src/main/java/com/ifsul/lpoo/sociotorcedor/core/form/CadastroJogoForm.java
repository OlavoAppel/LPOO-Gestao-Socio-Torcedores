package com.ifsul.lpoo.sociotorcedor.core.form;

import com.ifsul.lpoo.sociotorcedor.core.model.base.Estadio;
import com.ifsul.lpoo.sociotorcedor.core.model.base.LoteIngresso;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Time;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CadastroJogoForm {

    @NotNull(message = "Selecione o time da casa")
    private Long casaId;

    @NotNull(message = "Selecione o time visitante")
    private Long foraId;

    @NotNull(message = "Informe a data e hora do jogo")
    @Future(message = "A data do jogo deve ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dhJogo;

    @NotBlank(message = "Informe o campeonato")
    @Size(max = 100)
    private String campeonato;

    @NotNull(message = "Informe o estádio")
    private Long estadioId;

    @AssertTrue(message = "Os times da casa e visitante devem ser diferentes")
    public boolean isTimesDiferentes() {
        return casaId == null || foraId == null || !casaId.equals(foraId);
    }

}
