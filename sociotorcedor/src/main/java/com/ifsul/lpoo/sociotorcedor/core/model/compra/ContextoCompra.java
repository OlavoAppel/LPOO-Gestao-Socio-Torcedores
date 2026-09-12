package com.ifsul.lpoo.sociotorcedor.core.model.compra;

import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ContextoCompra {

    private Usuario usuario;
    private LocalDateTime dhRequisicao;


}
