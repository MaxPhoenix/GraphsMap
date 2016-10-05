package grafos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Grafo
{
	private ArrayList<Set<Integer>> vecinos;
	private int aristas;
	private int vertices;

	public Grafo(int n)
	{
		if (n < 0)
			throw new IllegalArgumentException("cantidad vertices negativo: " + n);
		
		vecinos = new ArrayList<Set<Integer>>();
		vertices = n;
		for(int i=0; i<n; ++i)
			vecinos.add( new HashSet<Integer>() );
	}

	public int vertices(){
		return vecinos.size();
	}

	public int aristas(){
		return aristas;
	}

	public void agregarArista(int i, int j){
		chequearExtremos(i,j);

		if (!contieneArista(i,j))
			aristas++;
		
		vecinos.get(i).add(j);
		vecinos.get(j).add(i);
	}

	public void removerArista(int i, int j){
		chequearExtremos(i, j);

		if (contieneArista(i,j))
			aristas--;
		
		vecinos.get(i).remove(j);
		vecinos.get(j).remove(i);
	}

	public boolean contieneArista(int i, int j){
		chequearExtremos(i,j);
		return vecinos.get(i).contains(j);
	}

	private void chequearExtremos(int i, int j){
		if (i <= -1 || j <= -1 || i >= vertices() || j >= vertices())
			throw new IllegalArgumentException("Vertices fuera de rango: " + i + ", " + j + " (vertices = " + vertices() + ")");

		if (i == j)
			throw new IllegalArgumentException("No se pueden agregar loops: " + i +" "+j);
	}

	public int gradoDelVertice(int v){
		return vecinos.get(v).size();
	}

	public Set<Integer> vecinos(int v){
		return vecinos.get(v); // Taraaan!
	}
	
	public int cantVertices(){
		return this.vertices;
	}
	
	@Override
	public String toString(){
		String grafo = "";
		for(int i = 0; i < this.vecinos.size(); i++){
			grafo += "Vertice: " + i + " Vecinos: " + this.vecinos.get(i).toString() + "\n";
		}
		return grafo;
	}
}
