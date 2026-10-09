package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.LoteIngressoService;
import com.ifsul.lpoo.sociotorcedor.core.model.compra.ContextoCompra;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}