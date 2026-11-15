package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.LoteIngressoService;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/ingressos")
public class LoteIngressoController {

    private final LoteIngressoService loteIngressoService;

    public LoteIngressoController(LoteIngressoService loteIngressoService) {
        this.loteIngressoService = loteIngressoService;
    }

    @GetMapping
    public String listarLotes(@RequestParam Long jogoId,
                              @AuthenticationPrincipal Usuario usuario,
                              Model model) {

        ContextoCompra contexto = new ContextoCompra();
        contexto.setDhRequisicao(LocalDateTime.now());
        contexto.setJogoId(jogoId);
        contexto.setUsuario(usuario);

        model.addAttribute("lotes", loteIngressoService.findLotes(contexto));
        model.addAttribute("logado", usuario != null);
        return "ingressos/lista";
    }

    @PostMapping("/comprar")
    public String comprarIngresso(@RequestParam Long loteId,
                                  @AuthenticationPrincipal Usuario usuario,
                                  RedirectAttributes redirectAttributes){
        ContextoCompra contexto = new ContextoCompra();
        contexto.setDhRequisicao(LocalDateTime.now());
        contexto.setUsuario(usuario);
        contexto.setLoteId(loteId);

        try {
            String resultado = loteIngressoService.comprarIngresso(contexto);
            if (!"sucesso".equals(resultado)) {
                redirectAttributes.addFlashAttribute("erro", "Não foi possível concluir a compra deste ingresso.");
                return "redirect:/jogos";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível concluir a compra deste ingresso.");
            return "redirect:/jogos";
        }
        return "redirect:/ingressos/sucesso"; // PRG: F5 não repete a compra
    }

    @GetMapping("/sucesso")
    public String sucesso() {
        return "sucesso";
    }

}