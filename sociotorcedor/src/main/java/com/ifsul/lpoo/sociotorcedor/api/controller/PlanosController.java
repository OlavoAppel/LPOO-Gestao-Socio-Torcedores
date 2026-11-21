package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.JogoDetalheService;
import com.ifsul.lpoo.sociotorcedor.api.service.MosaicoService;
import com.ifsul.lpoo.sociotorcedor.api.service.SocioTorcedorService;
import com.ifsul.lpoo.sociotorcedor.core.dto.CategoriaSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Comparação pública das categorias de sócio (só leitura). A associação continua em
 * POST /sociotorcedor/associar, que exige login; quem não entrou é levado ao login antes.
 */
@Controller
@RequiredArgsConstructor
public class PlanosController {

    private final SocioTorcedorService socioTorcedorService;
    private final MosaicoService mosaicoService;
    private final JogoDetalheService jogoDetalheService;

    @GetMapping("/planos")
    public String planos(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<CategoriaSocioDTO> categorias = socioTorcedorService.listarCategoriasDisponiveis();
        model.addAttribute("categorias", categorias);
        model.addAttribute("beneficiosComparados", DTOFactory.createLinhasBeneficio(categorias));
        model.addAttribute("mosaico", mosaicoService.montar(usuario));
        model.addAttribute("exemplo", jogoDetalheService.detalharProximo(usuario).orElse(null));
        return "planos";
    }

}
