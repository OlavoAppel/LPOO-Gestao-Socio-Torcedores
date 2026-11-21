package com.ifsul.lpoo.sociotorcedor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/** Notícia do clube (hoje vem de NoticiaMockService; ver docs/front/dados-ficticios.md). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticiaDTO {

    private Long id;
    private String titulo;
    private String resumo;
    private List<String> paragrafos;
    private LocalDate data;
    /** Seção da notícia: Clube, Torcida, Base, Sócio. */
    private String editoria;

}
