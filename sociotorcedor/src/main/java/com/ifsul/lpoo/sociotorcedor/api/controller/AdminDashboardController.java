package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.AdminDashboardService;
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

    @GetMapping
    public String painel() {
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
