package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.AdminDashboardService;
import com.ifsul.lpoo.sociotorcedor.core.dto.DashboardInfoDTO;
import org.hibernate.annotations.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private AdminDashboardService dashboardService;

    @GetMapping("perfil")
    public String perfil(){
        return "perfil";
    }

    @GetMapping("admin")
    public String dashBoard(){
        return "admin";
    }

    @GetMapping("/admin/metricas")
    public String dashboardMetricas(Model model){
        DashboardInfoDTO dashboardInfo = dashboardService.getInformacoesDashboard();
        model.addAttribute("dashboardInfo", dashboardInfo);

        return "admin/dashboard";
    }

}
