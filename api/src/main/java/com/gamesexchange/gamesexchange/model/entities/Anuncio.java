package com.gamesexchange.gamesexchange.model.entities;

import com.gamesexchange.gamesexchange.model.entities.enums.TipoAnuncio;
import com.gamesexchange.gamesexchange.model.entities.enums.TipoPrioridade;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Games Exchange API
 * Wender Galan
 * Todos os direitos reservados ©
 * *********************************************
 * Nome do arquivo: Anuncio.java
 * Criado por : Wender Galan
 * Data da criação : 17/10/2018
 * Observação :
 * *********************************************
 */
@Entity
@Table(name = "anuncios", schema = "public")
public class Anuncio implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "titulo", nullable = false)
    @Length(min = 1, max = 50, message = "O titulo deve ter no minímo {min} e no máximo {max} caracteres")
    @NotNull(message = "O titulo não foi informado.")
    private String titulo;

    @Column(name = "descricao", nullable = false)
    @Length(min = 1, max = 1000, message = "A descrição deve ter no minímo {min} e no máximo {max} caracteres")
    @NotNull(message = "O descrição não foi informado.")
    private String descricao;

    @Column(name = "valor")
    private Double valor;

    @NotNull(message = "O tipo do anúncio não pode ser nulo.")
    @Column(name = "tipo_anuncio", nullable = false)
    private TipoAnuncio tipoAnuncio;

    @Column(name = "pesquisa", nullable = false)
    @Length(max = 1050)
    private String pesquisa;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_anunciante", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_anuncio_anunciante"), nullable = false)
    private Usuario anunciante;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_anuncio_endereco"), nullable = false)
    private Endereco endereco;

    //LocalDateTime
    //data da inserção e hora da inserção

    @Column(name = "prioridade", nullable = false)
    private TipoPrioridade prioridade;

    @Column(name = "qtd_visitas", nullable = false)
    private int qtdVisitas;

    @Column(name = "qtd_denuncias", nullable = false)
    private int qtdDenuncias;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "anuncio")
    private List<Imagem> imagensAnuncio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoAnuncio getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(TipoAnuncio tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    public String getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }

    public Usuario getAnunciante() {
        return anunciante;
    }

    public void setAnunciante(Usuario anunciante) {
        this.anunciante = anunciante;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public TipoPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(TipoPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    public int getQtdVisitas() {
        return qtdVisitas;
    }

    public void setQtdVisitas(int qtdVisitas) {
        this.qtdVisitas = qtdVisitas;
    }

    public int getQtdDenuncias() {
        return qtdDenuncias;
    }

    public void setQtdDenuncias(int qtdDenuncias) {
        this.qtdDenuncias = qtdDenuncias;
    }

    public List<Imagem> getImagensAnuncio() {
        return imagensAnuncio;
    }

    public void setImagensAnuncio(List<Imagem> imagensAnuncio) {
        this.imagensAnuncio = imagensAnuncio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return qtdVisitas == anuncio.qtdVisitas &&
                qtdDenuncias == anuncio.qtdDenuncias &&
                Objects.equals(id, anuncio.id) &&
                Objects.equals(titulo, anuncio.titulo) &&
                Objects.equals(descricao, anuncio.descricao) &&
                Objects.equals(valor, anuncio.valor) &&
                tipoAnuncio == anuncio.tipoAnuncio &&
                Objects.equals(pesquisa, anuncio.pesquisa) &&
                Objects.equals(anunciante, anuncio.anunciante) &&
                Objects.equals(endereco, anuncio.endereco) &&
                prioridade == anuncio.prioridade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descricao, valor, tipoAnuncio, pesquisa, anunciante, endereco, prioridade, qtdVisitas, qtdDenuncias);
    }
}
