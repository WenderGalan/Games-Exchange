package gamesexchange.com.gamesexchange.model;

public class Anuncio {
	private String imagens [];
	private String titulo;
	private String descricao;
	private String tipo;
	private String categoria;
	private String dataDaInsercao;
	private String horarioDaInsercao;
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
	public int getcontadorDenuncia() {
		return contadorDenuncia;
	}
	public void setcontadorDenuncia(int contadorDenuncia) {
		this.contadorDenuncia = contadorDenuncia;
	}
	public String[] getImagens() {
		return imagens;
	}
	public void setImagens(String[] imagens) {
		this.imagens = imagens;
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
	
}