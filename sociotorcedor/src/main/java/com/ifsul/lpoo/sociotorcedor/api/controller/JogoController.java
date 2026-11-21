package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.CalendarioService;
import com.ifsul.lpoo.sociotorcedor.api.service.JogoService;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogosDisponiveisDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/jogos")
@RequiredArgsConstructor
public class JogoController {

    private final JogoService jogoService;
    private final CalendarioService calendarioService;

    @GetMapping
    public String jogosDisponiveis(Model model) {
        JogosDisponiveisDTO jogosData = jogoService.findJogosAno();
        model.addAttribute("jogosData", jogosData);

        // extras (só leitura): os mesmos jogos em ordem de data, agrupados por mês
        LocalDateTime agora = LocalDateTime.now();
        model.addAttribute("proximosPorMes", calendarioService.proximosPorMes(jogosData, agora));
        model.addAttribute("encerradosPorMes", calendarioService.encerradosPorMes(jogosData, agora));
        return "jogos";
    }
}
