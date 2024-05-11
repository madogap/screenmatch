package com.br.mado.ScreenMatchJpa.repository;

import com.br.mado.ScreenMatchJpa.model.Categoria;
import com.br.mado.ScreenMatchJpa.model.Episodio;
import com.br.mado.ScreenMatchJpa.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    //Fazer busca no banco do titulo da serie pelas primeiras letras
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeDaSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    //List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);

    //JPGL
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriePorTemporadaEAvaliaaco(int totalTemporadas, double avaliacao);

    //iLIKE  IGNORECASE, %O CONTAINS %:
    @Query("SELECT e from Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio% ")
    List<Episodio> episodioPorTrecho(String trechoEpisodio);

    @Query("SELECT e from Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5 ")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e from Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
