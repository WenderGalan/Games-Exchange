package gamesexchange.com.gamesexchange.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wender Galan Gamer on 21/02/2018.
 */

public final class ListaItens {

    private static List<String> listaTipoAnuncio;
    private static List<String> listaCategoria;
    private static List<String> listaComputadores;
    private static List<String> listaCelulares;
    private static List<String> listaModeloVideoGames;

    public static List<String> getListaTipoAnuncio() {
        listaTipoAnuncio = new ArrayList<String>();
        listaTipoAnuncio.add("Venda");
        listaTipoAnuncio.add("Troca");
        listaTipoAnuncio.add("Troca & Venda");
        listaTipoAnuncio.add("Tipo de Anúncio");
        return listaTipoAnuncio;
    }

    public static List<String> getListaCategoria() {
        listaCategoria = new ArrayList<String>();
        listaCategoria.add("Consoles e acessórios");
        listaCategoria.add("Jogos");
        listaCategoria.add("Computadores e acessórios");
        listaCategoria.add("Celular e telefonia");
        listaCategoria.add("Categoria");
        return listaCategoria;
    }

    public static List<String> getListaComputadores() {
        listaComputadores = new ArrayList<String>();
        listaComputadores.add("PCs e computadores");
        listaComputadores.add("Notebook e netbook");
        listaComputadores.add("Ipad e tablet");
        listaComputadores.add("Impressoras e suplementos");
        listaComputadores.add("Peças e acessórios");
        listaComputadores.add("Tipo");
        return listaComputadores;
    }

    public static List<String> getListaCelulares() {
        listaCelulares = new ArrayList<String>();
        listaCelulares.add("Apple");
        listaCelulares.add("Asus");
        listaCelulares.add("Samsung");
        listaCelulares.add("LG");
        listaCelulares.add("Nokia");
        listaCelulares.add("Motorola e Lenovo");
        listaCelulares.add("Sony");
        listaCelulares.add("Blackberry");
        listaCelulares.add("HTC");
        listaCelulares.add("Telefones e aparelhos de fax");
        listaCelulares.add("Acessórios");
        listaCelulares.add("Outros");
        listaCelulares.add("Tipo");
        return listaCelulares;
    }

    public static List<String> getListaModeloVideoGames() {
        listaModeloVideoGames = new ArrayList<String>();
        listaModeloVideoGames.add("Xbox One");
        listaModeloVideoGames.add("Xbox 360");
        listaModeloVideoGames.add("Xbox");
        listaModeloVideoGames.add("Playstation 4");
        listaModeloVideoGames.add("Playstation 3");
        listaModeloVideoGames.add("Playstation 2");
        listaModeloVideoGames.add("Playstation 1");
        listaModeloVideoGames.add("PS Vita");
        listaModeloVideoGames.add("PSP");
        listaModeloVideoGames.add("Wii U");
        listaModeloVideoGames.add("Wii");
        listaModeloVideoGames.add("Nintendo DS");
        listaModeloVideoGames.add("Game Boy");
        listaModeloVideoGames.add("Outros");
        listaModeloVideoGames.add("Tipo");
        return listaModeloVideoGames;
    }
}
