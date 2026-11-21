package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.AdminDashboardService;
import com.ifsul.lpoo.sociotorcedor.api.service.AdminJogosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;
    private final AdminJogosService adminJogosService;

    @GetMapping
    public String painel(Model model) {
        // extras (só leitura): números do mês e todos os jogos com a situação dos lotes e das vendas
        model.addAttribute("dashboardInfo", dashboardService.getInformacoesDashboard());
        model.addAttribute("jogos", adminJogosService.listarParaPainel());
        return "admin";
    }

    @GetMapping("/metricas")
    public String metricas(Model model) {
        model.addAttribute("dashboardInfo", dashboardService.getInformacoesDashboard());
        model.addAttribute("categoriasInfo", dashboardService.getInformacoesCategorias());
        model.addAttribute("jogosInfo", dashboardService.getInformacoesJogos());
        return "admin/dashboard";
    }

}
