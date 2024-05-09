package com.br.mado.ScreenMatchJpa.main;

import com.br.mado.ScreenMatchJpa.model.DadosSerie;
import com.br.mado.ScreenMatchJpa.model.DadosTemporada;
import com.br.mado.ScreenMatchJpa.model.Episodio;
import com.br.mado.ScreenMatchJpa.model.Serie;
import com.br.mado.ScreenMatchJpa.repository.SerieRepository;
import com.br.mado.ScreenMatchJpa.service.ConsumoApi;
import com.br.mado.ScreenMatchJpa.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b13e9aeb";

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    public Main(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao!=0){

            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar série por Título
                5 - Buscar séries por Ator
                0 - Sair                                 
                """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    ListarSerieBuscadas();
                    break;
                case 4 :
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    //Fazer busca das series do banco de dados
    private void buscarEpisodioPorSerie(){
        //DadosSerie dadosSerie = getDadosSerie();

        ListarSerieBuscadas();
        System.out.println("Escolha a série pelo nome:");
        var nomeDaSerie = leitura.nextLine();

        //Buscar do banco
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeDaSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream().
                            map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void ListarSerieBuscadas(){
       // List<Serie> series = new ArrayList<>();
        //series= dadosSeries.stream()
               // .map(d -> new Serie(d))
              //  .collect(Collectors.toList());
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha a série pelo titulo:");
        var nomeDaSerie = leitura.nextLine();
        Optional<Serie> serieBuscadaPorTitulo = repositorio.findByTituloContainingIgnoreCase(nomeDaSerie);

        if (serieBuscadaPorTitulo.isPresent()){
            System.out.println("Dados da série " + serieBuscadaPorTitulo);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator para busca? ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();
        //Um lista para todas series que o ator trabalha
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou");
        seriesEncontradas.forEach(serie -> System.out.println(serie.getTitulo() + " avaliação" + serie.getAvaliacao()));
        System.out.println(seriesEncontradas);
    }

}
