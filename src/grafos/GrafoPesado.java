package grafos;

import java.util.Set;

public class GrafoPesado
{
	private Grafo grafo;
	private double[][] pesos;
	
	public GrafoPesado(int n){
		grafo = new Grafo(n);
		pesos = new double[n][n];
	}
	
	public void agregarArista(int i, int j, double peso){
		grafo.agregarArista(i,j);
		pesos[i][j] = peso;
		pesos[j][i] = peso;
	}
	
	public boolean contieneArista(int i, int j){
		return grafo.contieneArista(i, j);
	}
	
	public void quitarArista(int i,int j){
		grafo.removerArista(i, j);
		pesos[i][j] = 0;
		pesos[j][i] = 0;
	}
	
	public double getPeso(int i, int j){
		if( grafo.contieneArista(i, j) == false )
			throw new IllegalArgumentException("Se consulto el peso de una arista inexistente! " + i + ", " + j);

		return pesos[i][j];		
	}

	// Consultas
	public int vertices(){
		return grafo.vertices();
	}
	
	public int aristas(){
		return grafo.aristas();
	}
	
	public Set<Integer> vecinos(int i){
		return grafo.vecinos(i);
	}
	
	public int cantVertices(){
		return grafo.cantVertices();
	}
	
	@Override
	public String toString(){
		return this.grafo.toString();
	}
}
