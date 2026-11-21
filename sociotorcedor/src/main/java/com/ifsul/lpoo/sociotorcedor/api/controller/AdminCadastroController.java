package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.AdminCadastroService;
import com.ifsul.lpoo.sociotorcedor.api.service.AdminJogosService;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroJogoForm;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroLoteForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cadastro")
@RequiredArgsConstructor
public class AdminCadastroController {

    private final AdminCadastroService adminCadastroService;
    private final AdminJogosService adminJogosService; // só leitura: sugestões de campeonato

    @GetMapping("/jogo")
    public String formNovoJogo(Model model) {
        model.addAttribute("cadastroJogoForm", new CadastroJogoForm());
        carregarOpcoesJogo(model);
        return "admin/cadastro/jogos/form";
    }

    @PostMapping("/jogo")
    public String salvarJogo(@Valid @ModelAttribute("cadastroJogoForm") CadastroJogoForm form,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            carregarOpcoesJogo(model);
            return "admin/cadastro/jogos/form";
        }
        Jogo jogo = adminCadastroService.cadastrarJogo(form);
        return "redirect:/admin/cadastro/" + jogo.getId() + "/lotes";
    }

    @GetMapping("/{jogoId}/lotes")
    public String listarLotes(@PathVariable Long jogoId, Model model) {
        carregarTelaLotes(jogoId, model);
        model.addAttribute("cadastroLoteForm", new CadastroLoteForm());
        return "admin/cadastro/jogos/lotes";
    }

    @PostMapping("/{jogoId}/lotes")
    public String salvarLote(@PathVariable Long jogoId,
                             @Valid @ModelAttribute("cadastroLoteForm") CadastroLoteForm form,
                             BindingResult result,
                             Model model) {
        if (!result.hasErrors()) {
            try {
                adminCadastroService.cadastrarLote(jogoId, form);
                return "redirect:/admin/cadastro/" + jogoId + "/lotes"; // PRG: F5 não reenvia o POST
            } catch (IllegalArgumentException e) {
                result.reject("lote.invalido", e.getMessage()); // erro global (regra de negócio)
            }
        }
        carregarTelaLotes(jogoId, model);
        model.addAttribute("abrirModal", true); // reabre o pop-up mostrando os erros
        return "admin/cadastro/jogos/lotes";
    }

    private void carregarOpcoesJogo(Model model) {
        model.addAttribute("times", adminCadastroService.listarTimes());
        model.addAttribute("estadios", adminCadastroService.listarEstadios());
        model.addAttribute("campeonatos", adminJogosService.listarCampeonatos()); // extra: sugestões do campo
    }

    private void carregarTelaLotes(Long jogoId, Model model) {
        model.addAttribute("jogo", adminCadastroService.buscarJogo(jogoId));
        model.addAttribute("lotes", adminCadastroService.listarLotes(jogoId));
        model.addAttribute("setores", adminCadastroService.listarSetores());
    }

}
