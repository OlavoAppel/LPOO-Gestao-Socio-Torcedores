package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.JogoService;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogosDisponiveisDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jogos")
@RequiredArgsConstructor
public class JogoController {

    private final JogoService jogoService;

    @GetMapping
    public String jogosDisponiveis(Model model) {
        JogosDisponiveisDTO jogosData = jogoService.findJogosAno();
        model.addAttribute("jogosData", jogosData);
        return "jogos";
    }
}