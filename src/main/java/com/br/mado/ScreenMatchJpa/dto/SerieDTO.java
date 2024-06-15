package com.br.mado.ScreenMatchJpa.dto;

import com.br.mado.ScreenMatchJpa.model.Categoria;

public record SerieDTO(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String atores,
        String poster,
        String sinopse
        ) {
}
