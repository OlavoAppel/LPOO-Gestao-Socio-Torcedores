package com.ifsul.lpoo.sociotorcedor.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("perfil")
    public String perfil(){
        return "perfil";
    }

    @GetMapping("admin/dashboard")
    public String dashBoard(){
        return "admindashboard";
    }

}
