package grafos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Grafo
{
	private ArrayList<Set<Integer>> vecinos;
	private int aristas;
	private int vertices;

	public Grafo(int n)
	{
		if (n < 0)
			throw new IllegalArgumentException("cantidad vertices negativo: " + n);
		
		vecinos = new ArrayList<> ();
		vertices = n;
		for(int i=0; i<n; ++i)
			vecinos.add(new HashSet<> () );
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

	public void removerArista(){
		chequearExtremos(2, 0);

		if (contieneArista(2, 0))
			aristas--;
		
		vecinos.get(2).remove(0);
		vecinos.get(0).remove(2);
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
