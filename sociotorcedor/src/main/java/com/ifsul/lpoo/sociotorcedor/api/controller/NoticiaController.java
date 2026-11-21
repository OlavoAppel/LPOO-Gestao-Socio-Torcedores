package com.ifsul.lpoo.sociotorcedor.api.controller;

import com.ifsul.lpoo.sociotorcedor.api.service.NoticiaService;
import com.ifsul.lpoo.sociotorcedor.core.dto.NoticiaDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/** Notícias do clube (públicas). Depende só da interface NoticiaService (hoje com dados fictícios). */
@Controller
@RequestMapping("/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService noticiaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("noticias", noticiaService.listar());
        return "noticias/lista";
    }

    @GetMapping("/{id:\\d+}")
    public String detalhe(@PathVariable Long id, Model model, HttpServletResponse response) {
        Optional<NoticiaDTO> noticia = noticiaService.buscar(id);
        if (noticia.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("titulo", "Notícia não encontrada");
            model.addAttribute("mensagem", "Essa notícia não existe ou saiu do ar. As mais recentes estão na lista de notícias.");
            model.addAttribute("destino", "/noticias");
            model.addAttribute("acao", "Ver as notícias");
            return "nao-encontrado";
        }
        model.addAttribute("noticia", noticia.get());
        model.addAttribute("outras", noticiaService.recentes(4).stream()
                .filter(n -> !n.getId().equals(id)).limit(3).toList());
        return "noticias/detalhe";
    }

}
