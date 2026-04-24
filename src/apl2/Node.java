public class Node {

	// dados da pessoa a serem armazenados no Node (nó)
	private String id;
	private String nome;
	private float nota;
	
	// ponteiro para o próximo/anterior Node (nó) da lista ligada
	private Node prox;
	private Node ant;
	
	// construtor vazio
	public Node() {}
	
	// construtor com parâmetros
	public Node(String id, String nome, float nota) {
		this.id = id;
		this.nome = nome;
		this.nota = nota;
		this.prox = null;
		this.ant = null;
	}
	
	// getters (obtém) e setters (atribui) dos dados da pessoa
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public float getNota() { return nota; }
	public void setNota(float nota) { this.nota = nota; }
	
	// getProx(): obtém o ponteiro para o próximo Node (nó) da lista ligada
	public Node getProx() { return prox; }
	// setProx(): atribui o endereço do próximo Node (nó) da lista ligada
	public void setProx(Node prox) { this.prox = prox; }
	
	// getAnt(): obtém o ponteiro para o Node (nó) anterior da lista ligada
	public Node getAnt() { return ant; }
	// setAnt(): atribui o endereço do Node (nó) anterior da lista ligada
	public void setAnt(Node ant) { this.ant = ant; }
	
	@Override
	public String toString() {
		return id + ";" + nome + ";" + nota;
	}
}
