package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.SocioTorcedorService;
import com.ifsul.lpoo.sociotorcedor.core.form.AssociarForm;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SocioTorcedorController {

    @Autowired
    private SocioTorcedorService socioTorcedorService;

    @GetMapping("/sociotorcedor")
    private String socioTorcedor(){
        return "sociotorcedor";
    }

    @PostMapping("/sociotorcedor/associar")
    private void associar(@AuthenticationPrincipal Usuario usuario, @Valid @ModelAttribute AssociarForm associarForm){
        socioTorcedorService.cadastrarSocio(associarForm, usuario);
    }

}
