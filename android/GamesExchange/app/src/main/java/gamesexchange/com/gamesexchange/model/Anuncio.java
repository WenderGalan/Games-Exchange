package gamesexchange.com.gamesexchange.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import gamesexchange.com.gamesexchange.config.ConfiguracaoFirebase;

public class Anuncio {
	private String id;
	private String imagens;
	private String titulo;
	private String descricao;
	private String tipo;
	private String tipoAnuncio;
	private String categoria;
	private String dataDaInsercao;
	private String horarioDaInsercao;
	private String tags;
	private String valor;
	private int contadorDenuncia;
	private int visitas;
	private String cep;
	private String estado;
	private String cidade;
	/**
	 * ATRIBUTO - PRIORIDADE
	 * O atributo prioridade a principio para todo anuncio irá receber 0, sendo:
	 * 0-Prioridade Baixa - Gratu�to
	 * 1- Prioridade Baixa-Pago
	 * 2-Prioridade M�dia-Pago
	 * 3-Prioridade Alta-Pago
	 * **/
	private int prioridade;

	/**CONSTRUTOR**/
	public Anuncio() {
		super();
	}
	
	/**GETTERS AND SETTERS**/
	public void setContadorDenuncia(int contadorDenuncia) {
		this.contadorDenuncia = contadorDenuncia;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getDataDaInsercao() {
		return dataDaInsercao;
	}
	public void setDataDaInsercao(String dataDaInsercao) {
		this.dataDaInsercao = dataDaInsercao;
	}
	public String getHorarioDaInsercao() {
		return horarioDaInsercao;
	}
	public void setHorarioDaInsercao(String horarioDaInsercao) {
		this.horarioDaInsercao = horarioDaInsercao;
	}
	public int getVisitas() {
		return visitas;
	}
	public void setVisitas(int visitas) {
		this.visitas = visitas;
	}
	public int getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	public int getContadorDenuncia() {
		return contadorDenuncia;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImagens() {
		return imagens;
	}
	public void setImagens(String imagens) {
		this.imagens = imagens;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipoAnuncio() {
		return tipoAnuncio;
	}
	public void setTipoAnuncio(String tipoAnuncio) {
		this.tipoAnuncio = tipoAnuncio;
	}

	public void salvar(){
		DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
		referenciaFirebase.child("anuncios").child(getId()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				Log.i("DEBUG", "Anúncio salvo com sucesso");
			}
		});
	}
}