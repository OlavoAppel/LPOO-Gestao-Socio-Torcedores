package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.repository.IngressoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    private final IngressoRepository ingressoRepository;

    public PerfilController(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    @GetMapping("/perfil")
    public String perfil(Authentication auth, Model model) {
        model.addAttribute("ingressos", ingressoRepository.buscarDoUsuario(auth.getName()));
        return "perfil";
    }

}
