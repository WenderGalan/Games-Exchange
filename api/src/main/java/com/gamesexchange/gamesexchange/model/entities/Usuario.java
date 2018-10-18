package com.gamesexchange.gamesexchange.model.entities;

import com.gamesexchange.gamesexchange.model.entities.enums.TipoUsuario;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Games Exchange API
 * Wender Galan
 * Todos os direitos reservados ©
 * *********************************************
 * Nome do arquivo: Usuario.java
 * Criado por : Wender Galan
 * Data da criação : 17/10/2018
 * Observação :
 * *********************************************
 */
@Entity
@Table(name = "usuarios", schema = "public")
public class Usuario {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    @NotNull(message = "O nome não poder nulo.")
    @Length(min = 1, max = 100, message = "O nome deve ter no minímo {min} e no máximo {max} caracteres.")
    @Column(name = "nome", nullable = false)
    private String nome;

    @Length(min = 1, max = 11, message = "O telefone deve ter no máximo {max} caracteres.")
    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email", nullable = false)
    @Length(min = 1, max = 200, message = "O email deve ter no minímo {min} e no máximo {max} caracteres")
    @NotNull(message = "O email não foi informado.")
    @Email
    private String email;

    @Column(name = "senha", nullable = false)
    @Length(min = 1, max = 50, message = "A senha deve ter no minímo {min} e no máximo {max} caracteres")
    @NotNull(message = "A senha não foi informada.")
    private String senha;

    @Column(name = "tipo_usuario", nullable = false)
    @NotNull(message = "O tipo do usuário não foi informado.")
    private TipoUsuario tipoUsuario;

    @Column(name = "anuncios_removidos", nullable = false)
    private int anunciosRemovidos;

    @Column(name = "url_imagem")
    private String urlImagem = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
