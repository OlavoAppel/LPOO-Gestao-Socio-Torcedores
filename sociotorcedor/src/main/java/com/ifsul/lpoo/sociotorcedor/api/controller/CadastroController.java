package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.CadastroService;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CadastroController {

    @Autowired
    private CadastroService cadastroService;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("cadastroForm", new CadastroForm());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processaCadastro(@Valid @ModelAttribute CadastroForm cadastroForm, BindingResult result, RedirectAttributes redirectAttributes){
        /*if (result.hasErrors()) {
            return "cadastro"; // volta pro form mostrando os erros de validação
        }*/

        try{
            cadastroService.cadastrar(cadastroForm);
            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado!");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }

}
