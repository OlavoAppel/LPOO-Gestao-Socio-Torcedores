package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.JogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JogoService jogoService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("jogosData", jogoService.findJogosAno());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
