package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.AreaSocioService;
import com.ifsul.lpoo.sociotorcedor.api.service.FidelidadeService;
import com.ifsul.lpoo.sociotorcedor.api.service.JogoDetalheService;
import com.ifsul.lpoo.sociotorcedor.api.service.MosaicoService;
import com.ifsul.lpoo.sociotorcedor.api.service.SocioTorcedorService;
import com.ifsul.lpoo.sociotorcedor.core.dto.CategoriaSocioDTO;
import com.ifsul.lpoo.sociotorcedor.core.dto.MosaicoDTO;
import com.ifsul.lpoo.sociotorcedor.core.exception.SocioTorcedorException;
import com.ifsul.lpoo.sociotorcedor.core.factory.DTOFactory;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Associado;
import com.ifsul.lpoo.sociotorcedor.core.model.user.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sociotorcedor")
@RequiredArgsConstructor
public class SocioTorcedorController {

    private final SocioTorcedorService socioTorcedorService;
    private final MosaicoService mosaicoService;
    private final JogoDetalheService jogoDetalheService;
    private final AreaSocioService areaSocioService;
    private final FidelidadeService fidelidadeService;

    @GetMapping
    public String socioTorcedor(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<CategoriaSocioDTO> categorias = socioTorcedorService.listarCategoriasDisponiveis();
        model.addAttribute("categorias", categorias);

        // benefícios da categoria atual, para o sócio ver o que já recebe
        if (usuario != null && usuario.getAssociado() != null && !usuario.getAssociado().getCategoria().isNaoAssociado()) {
            model.addAttribute("beneficiosAtuais",
                    socioTorcedorService.listarBeneficios(usuario.getAssociado().getCategoria().getId()));
        }

        MosaicoDTO mosaico = mosaicoService.montar(usuario);
        model.addAttribute("mosaico", mosaico);
        model.addAttribute("beneficiosComparados", DTOFactory.createLinhasBeneficio(categorias));
        model.addAttribute("exemplo", jogoDetalheService.detalharProximo(usuario).orElse(null));
        if (usuario != null && usuario.getAssociado() != null) {
            Associado associado = usuario.getAssociado();
            model.addAttribute("historico", areaSocioService.historico(associado.getId()));
            if (!associado.getCategoria().isNaoAssociado()) {
                model.addAttribute("carteirinha", areaSocioService.carteirinha(associado, mosaico));
                model.addAttribute("pontos", fidelidadeService.resumoDo(usuario));
            }
        }
        return "sociotorcedor";
    }

    @PostMapping("/associar")
    public String associar(@AuthenticationPrincipal Usuario usuario,
                           @RequestParam Long categoriaId,
                           RedirectAttributes redirectAttributes) {
        try {
            socioTorcedorService.tornarseSocio(categoriaId, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Associação realizada. Bem-vindo ao quadro de sócios!");
        } catch (SocioTorcedorException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/sociotorcedor";
    }

    @PostMapping("/revogarassociacao")
    public String revogarAssociacao(@AuthenticationPrincipal Usuario usuario,
                                    RedirectAttributes redirectAttributes) {
        try {
            socioTorcedorService.revogarAssociacao(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Associação cancelada.");
        } catch (SocioTorcedorException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/sociotorcedor";
    }

}
