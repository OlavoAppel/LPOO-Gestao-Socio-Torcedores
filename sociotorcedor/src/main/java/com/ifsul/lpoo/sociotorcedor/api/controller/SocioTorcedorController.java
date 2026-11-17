package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.SocioTorcedorService;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SocioTorcedorController {

    @Autowired
    private SocioTorcedorService socioTorcedorService;

    @GetMapping("/sociotorcedor")
    public String socioTorcedor(Model model){
        model.addAttribute("categorias", socioTorcedorService.listarCategoriasDisponiveis());
        return "sociotorcedor";
    }

    @PostMapping("/sociotorcedor/associar")
    public String associar(@AuthenticationPrincipal Usuario usuario,
                           @RequestParam Long categoriaId,
                           RedirectAttributes redirectAttributes){
        try {
            socioTorcedorService.tornarseSocio(categoriaId, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Associação realizada. Bem-vindo ao quadro de sócios!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/sociotorcedor";
    }

    @PostMapping("/sociotorcedor/revogarassociacao")
    public String revogarAssociacao(@AuthenticationPrincipal Usuario usuario,
                                    RedirectAttributes redirectAttributes){
        try {
            socioTorcedorService.revogarAssociacao(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Associação cancelada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/sociotorcedor";
    }

}
