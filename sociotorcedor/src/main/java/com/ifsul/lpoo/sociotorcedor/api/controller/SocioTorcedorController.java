package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.SocioTorcedorService;
import com.ifsul.lpoo.sociotorcedor.core.model.enumerator.Categoria;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SocioTorcedorController {

    @Autowired
    private SocioTorcedorService socioTorcedorService;

    @GetMapping("/sociotorcedor")
    private String socioTorcedor(){
        return "sociotorcedor";
    }

    @PostMapping("/sociotorcedor/associar")
    private void associar(@AuthenticationPrincipal Usuario usuario, @RequestParam Categoria categoria){
        socioTorcedorService.tornarseSocio(categoria, usuario);
    }

}
