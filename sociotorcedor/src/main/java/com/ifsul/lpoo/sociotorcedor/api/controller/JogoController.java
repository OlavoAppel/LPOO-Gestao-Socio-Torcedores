package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.core.dto.JogosDisponiveisDTO;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jogos")
public class JogoController {



    @GetMapping
    public String jogosDisponiveis(){
        return "jogos";
    }



}
