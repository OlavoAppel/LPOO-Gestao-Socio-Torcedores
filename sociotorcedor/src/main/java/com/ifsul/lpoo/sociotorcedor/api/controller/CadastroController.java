package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.CadastroService;
import com.ifsul.lpoo.sociotorcedor.core.exception.CadastroException;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CadastroController {

    private final CadastroService cadastroService;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("cadastroForm", new CadastroForm());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processaCadastro(@Valid @ModelAttribute("cadastroForm") CadastroForm cadastroForm,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            return "cadastro"; // reexibe o formulário com os erros de cada campo
        }
        try{
            cadastroService.cadastrar(cadastroForm);
            redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado! Entre com seu usuário e senha.");
            return "redirect:/login";
        } catch (CadastroException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastro";
        }
    }

}
