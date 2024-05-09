package com.br.mado.ScreenMatchJpa.repository;

import com.br.mado.ScreenMatchJpa.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    //Fazer busca no banco do titulo da serie pelas primeiras letras
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeDaSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

}
