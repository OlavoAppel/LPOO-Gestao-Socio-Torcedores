package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.JogoDetalheService;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogoDetalheDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/** Página pública de um jogo: data, estádio, setores à venda e quanto cada categoria paga (só leitura). */
@Controller
@RequiredArgsConstructor
public class JogoDetalheController {

    private final JogoDetalheService jogoDetalheService;

    @GetMapping("/jogos/{jogoId:\\d+}")
    public String detalhe(@PathVariable Long jogoId,
                          @AuthenticationPrincipal Usuario usuario,
                          Model model,
                          HttpServletResponse response) {
        Optional<JogoDetalheDTO> detalhe = jogoDetalheService.detalhar(jogoId, usuario);
        if (detalhe.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("titulo", "Jogo não encontrado");
            model.addAttribute("mensagem", "Esse jogo não existe ou foi removido. Os próximos estão no calendário.");
            model.addAttribute("destino", "/jogos");
            model.addAttribute("acao", "Ver o calendário");
            return "nao-encontrado";
        }
        model.addAttribute("detalhe", detalhe.get());
        return "jogos/detalhe";
    }

}
