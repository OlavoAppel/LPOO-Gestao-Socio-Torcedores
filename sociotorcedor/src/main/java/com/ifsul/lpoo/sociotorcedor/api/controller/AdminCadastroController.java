package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.repository.JogoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.LoteIngressoRepository;
import com.ifsul.lpoo.sociotorcedor.api.repository.TimeRepository;
import com.ifsul.lpoo.sociotorcedor.api.service.AdminCadastroService;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroJogoForm;
import com.ifsul.lpoo.sociotorcedor.core.form.CadastroLoteForm;
import com.ifsul.lpoo.sociotorcedor.core.model.base.Jogo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cadastro")
public class AdminCadastroController {

    @Autowired
    private AdminCadastroService adminCadastroService;

    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private JogoRepository jogoRepository;
    @Autowired
    private LoteIngressoRepository loteIngressoRepository;

    @GetMapping("/jogo")
    public String formNovo(Model model) {
        model.addAttribute("cadastroJogoForm", new CadastroJogoForm());
        model.addAttribute("times", timeRepository.findAll());
        return "admin/cadastro/jogos/form";
    }

    @PostMapping("/jogo")
    public String salvar(@Valid @ModelAttribute("cadastroJogoForm") CadastroJogoForm form,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("times", timeRepository.findAll());
            return "admin/cadastro/jogos/form";
        }

        Jogo jogo = adminCadastroService.cadastrarJogo(form);
        return "redirect:/admin/jogo/" + jogo.getId() + "/lotes";
    }


//    @GetMapping("admin/cadastro/{jogoId}/lotes")
//    public String listar(@PathVariable Long jogoId, Model model) {
//        carregarTela(jogoId, model);
//        model.addAttribute("cadastroLoteForm", new CadastroLoteForm());
//        return "admin/jogos/lotes";
//    }

//    @PostMapping("/{jogoId}/lotes")
//    public String salvar(@PathVariable Long jogoId,
//                         @Valid @ModelAttribute("cadastroLoteForm") CadastroLoteForm form,
//                         BindingResult result,
//                         Model model) {
//
//        if (!result.hasErrors()) {
//            try {
//                adminCadastroService.cadastrarLote(jogoId, form);
//                return "redirect:/admin/jogos/" + jogoId + "/lotes"; // PRG: F5 não reenvia o POST
//            } catch (IllegalArgumentException e) {
//                result.reject("lote.invalido", e.getMessage()); // erro global (regra de negócio)
//            }
//        }
//
//        carregarTela(jogoId, model);
//        model.addAttribute("abrirModal", true); // reabre o pop-up mostrando os erros
//        return "admin/jogos/lotes";
//    }
//
//    private void carregarTela(Long jogoId, Model model) {
//        model.addAttribute("jogo", jogoRepository.findById(jogoId));
//        model.addAttribute("lotes", loteIngressoRepository.findAllById(jogoId));
//        //model.addAttribute("setores", setorEstadioRepository.findAll());
//    }
}
