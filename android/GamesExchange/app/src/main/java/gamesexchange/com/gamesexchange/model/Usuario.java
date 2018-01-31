package gamesexchange.com.gamesexchange.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;

public class Usuario {
	private String id;
	private String nome;
	private String telefone;
	private String endereco;
	private String email;
	private String senha;
	private String foto; //URL da imagem
	private int anunciosRemovidos;
	//private Location lastLocation; //objeto localiza��o do usu�rio
	
	/**CONSTRUTOR**/
	public Usuario() {
	}

	/**GETTERS AND SETTERS**/
	@Exclude
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	@Exclude
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getAnunciosRemovidos() {
		return anunciosRemovidos;
	}
	public void setAnunciosRemovidos(int anunciosRemovidos) {
		this.anunciosRemovidos = anunciosRemovidos;
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
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}

	public void salvar(){
		DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
		referenciaFirebase.child("usuarios").child(getId()).setValue(this);
	}


}
