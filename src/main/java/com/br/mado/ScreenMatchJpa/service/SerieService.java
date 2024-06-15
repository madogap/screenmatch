package com.br.mado.ScreenMatchJpa.service;

import com.br.mado.ScreenMatchJpa.dto.SerieDTO;
import com.br.mado.ScreenMatchJpa.model.Serie;
import com.br.mado.ScreenMatchJpa.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasAsSeries(){
        return  converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Serie(){
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

            if (serie.isPresent()){
                Serie s = serie.get();
                return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());
            }
            return null;
    }

    public List<SerieDTO> obterLancamentos(){
        return converteDados(repositorio.lancamentoMaisRecente());
    }

}
