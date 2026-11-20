package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.LoteIngressoService;
import com.ifsul.lpoo.sociotorcedor.core.exception.CompraIngressoException;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LoteIngressoController {

    private final LoteIngressoService loteIngressoService;

    @GetMapping
    public String listarLotes(@RequestParam Long jogoId,
                              @AuthenticationPrincipal Usuario usuario,
                              Model model) {
        ContextoCompra contexto = novoContexto(usuario);
        contexto.setJogoId(jogoId);

        model.addAttribute("lotes", loteIngressoService.findLotes(contexto));
        model.addAttribute("logado", usuario != null);
        return "ingressos/lista";
    }

    @PostMapping("/comprar")
    public String comprarIngresso(@RequestParam Long loteId,
                                  @AuthenticationPrincipal Usuario usuario,
                                  RedirectAttributes redirectAttributes) {
        ContextoCompra contexto = novoContexto(usuario);
        contexto.setLoteId(loteId);

        try {
            loteIngressoService.comprarIngresso(contexto);
        } catch (CompraIngressoException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/jogos";
        }
        return "redirect:/ingressos/sucesso"; // PRG: F5 não repete a compra
    }

    @GetMapping("/sucesso")
    public String sucesso() {
        return "sucesso";
    }

    private ContextoCompra novoContexto(Usuario usuario) {
        ContextoCompra contexto = new ContextoCompra();
        contexto.setDhRequisicao(LocalDateTime.now());
        contexto.setUsuario(usuario);
        return contexto;
    }

}
