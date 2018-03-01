package gamesexchange.com.gamesexchange.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;

/**
 * A classe implementa a interface Serializable por que necessita passar um objeto como parametro a proxima activity
 * **/

public class Usuario implements Serializable, Cloneable{
	private String id;
	private String nome;
	private String telefone;
	private String cep;
	private String cidade;
	private String estado;
	private String email;
	private String senha;
	private String foto;
	private int anunciosRemovidos;
	
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
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void salvar(){
		DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
		referenciaFirebase.child("usuarios").child(getId()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				Log.i("DEBUG", "Usuário salvo com sucesso");
			}
		});
	}

	public Usuario clone() throws CloneNotSupportedException {
		return (Usuario) super.clone();
	}


}
