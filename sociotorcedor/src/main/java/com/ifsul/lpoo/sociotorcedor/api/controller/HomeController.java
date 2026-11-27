package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.CalendarioService;
import com.ifsul.lpoo.sociotorcedor.api.service.JogoService;
import com.ifsul.lpoo.sociotorcedor.api.service.MosaicoService;
import com.ifsul.lpoo.sociotorcedor.api.service.NoticiaService;
import com.ifsul.lpoo.sociotorcedor.api.service.SocioTorcedorService;
import com.ifsul.lpoo.sociotorcedor.core.dto.JogosDisponiveisDTO;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JogoService jogoService;
    private final CalendarioService calendarioService;
    private final MosaicoService mosaicoService;
    private final SocioTorcedorService socioTorcedorService;
    private final NoticiaService noticiaService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal Usuario usuario, Model model){
        JogosDisponiveisDTO jogosData = jogoService.findJogosAno();
        model.addAttribute("jogosData", jogosData);

        Long proximoId = jogosData.getProximoJogo() == null ? null : jogosData.getProximoJogo().getId();
        model.addAttribute("seguintes", calendarioService.proximos(jogosData, LocalDateTime.now(), proximoId, 3));
        model.addAttribute("mosaico", mosaicoService.montar(usuario));
        model.addAttribute("planos", socioTorcedorService.listarCategoriasDisponiveis());
        model.addAttribute("noticias", noticiaService.recentes(3));
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
