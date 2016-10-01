package grafos;

import java.util.HashSet;
import java.util.Set;

public class Algoritmos
{
	// Algoritmo de Prim
	public static GrafoPesado AGM(GrafoPesado grafo){
		GrafoPesado arbol = new GrafoPesado(grafo.vertices());
		Set<Integer> visitados = new HashSet<Integer>();
		visitados.add(0); // Cualquiera
		
		for(int i=0; i<grafo.vertices()-1; ++i){
			Arista a = menorArista(grafo, visitados); // De un amarillo a un negro
			arbol.agregarArista(a.origen, a.destino, a.peso);
			visitados.add(a.destino);
		}
		
		return arbol;
	}
	
	// Inner class

	public static GrafoPesado Clusters(GrafoPesado gp) {


		Arista ar = promedio(gp);

		gp.quitarArista(ar.origen, ar.destino);
		return gp;
	}

	// Retorna la arista de menor peso entre un vertice amarillo y uno no amarillo
	static Arista menorArista(GrafoPesado grafo, Set<Integer> visitados){
		Arista ret = new Arista(0, 0, Double.MAX_VALUE);

		for(Integer i: visitados)
			for (Integer j : grafo.vecinos(i))
				if( visitados.contains(j) == false ){
					if( grafo.getPeso(i, j) < ret.peso )
						ret = new Arista(i, j, grafo.getPeso(i, j));
				}
		return ret;
	}

	/**
	 * busca dentro del arbol la arista maxima, tomando un conjunto de aristas que contiene al maximo,
	 * y otro que tiene a los vertices utilizados previamente
	 * @param arbol
	 * @param verticesUsed los vertices usados para las aristas maximas de visited
	 * @return
	 */
	static Arista maxArista(GrafoPesado arbol, Set<Arista> MaxVisited,Set<Integer> verticesUsed){
		Arista max = new Arista(0,0,Double.MIN_VALUE);
		Arista aux = null;
		double peso = 0;

		int vertice1 = 0, vertice2 = 0;

		for(int i = 0; i < arbol.cantVertices();i++){
			for(Integer j : arbol.vecinos(i)){
				peso = arbol.getPeso(i, j);
				if(max.peso < peso){
					aux = new Arista (i,j,peso);
					boolean esta = MaxVisited.contains(aux);
					if(esta == false){
						//esto es para evitar que agarre otra arista igual pero en el sentido inverso
						if (verticesUsed.contains(i) == false || verticesUsed.contains(j) == false) {
							max = new Arista(i,j,peso);
							vertice1 = i;
							vertice2 = j;
						}
					}
				}
			}
		}
		if(max.peso != Double.MAX_VALUE){
			MaxVisited.add(max);
			verticesUsed.add(vertice1);
			verticesUsed.add(vertice2);
		}
		return max;
	}
	
	/**
	 * busca dentro del arbol la arista minima, tomando un conjunto de aristas que contiene al minimo,
	 * y otro que tiene a los vertices utilizados previamente
	 * @param arbol
	 * @param verticesUsed los vertices usados para las aristas minimas de visited
	 * @return
	 */
	static Arista minArista(GrafoPesado arbol, Set<Arista> minVisited,Set<Integer> verticesUsed){
		Arista min = new Arista(0,0,Double.MAX_VALUE);
		Arista aux = null;
		double peso = 0;

		int vertice1 = 0, vertice2 = 0;

		for(int i = 0; i < arbol.cantVertices();i++){
			for(Integer j : arbol.vecinos(i)){
				peso = arbol.getPeso(i, j);
				if(min.peso > peso){
					aux = new Arista (i,j,peso);
					boolean esta = minVisited.contains(aux);
					if(esta == false){
						//esto es para evitar que agarre otra arista igual pero en el sentido inverso
						if (verticesUsed.contains(i) == false || verticesUsed.contains(j) == false) {
							min = new Arista(i,j,peso);
							verticesUsed.add(i);
							verticesUsed.add(j);
						}
					}
				}
			}
		}
		//si cambio minimo modifica los conjuntos
		if(min.peso != Double.MIN_VALUE){
			minVisited.add(min);
			verticesUsed.add(vertice1);
			verticesUsed.add(vertice2);
		}
		return min;
	}

	static Arista promedio(GrafoPesado arbol) {
		Set<Arista> maxVisited = new HashSet<Arista>();
		Set<Arista> minVisited = new HashSet<Arista>();
		Set<Integer> maxVerticesUsed = new HashSet<Integer>();
		Set<Integer> minVerticesUsed = new HashSet<Integer>();

		for (int x = arbol.aristas(); x > 2; x -= 2) {

			Arista max = maxArista(arbol, maxVisited, maxVerticesUsed);
			Arista min = maxArista(arbol, minVisited, minVerticesUsed);
		}
		return maxArista(arbol, maxVisited, maxVerticesUsed);
	}

	static class Arista {
		public Integer origen;
		public Integer destino;
		public double peso;

		public Arista(int origen, int destino, double pesoArista) {
			this.origen = origen;
			this.destino = destino;
			this.peso = pesoArista;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;

			if (obj == null)
				return false;

			if (obj instanceof Arista) {
				Arista otra = (Arista) obj;
				if (origen == otra.origen && destino == otra.destino)
					return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.origen.hashCode() + this.destino.hashCode();
		}
	}
	
	
	

	
}









