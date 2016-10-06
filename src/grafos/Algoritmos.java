package grafos;

import java.util.HashSet;
import java.util.Set;

public class Algoritmos
{
	// Algoritmo de Prim
	public static GrafoPesado AGM(GrafoPesado grafo){
		GrafoPesado GRAFO = new GrafoPesado(grafo.vertices());
		Set<Integer> visitados = new HashSet<Integer>();
		visitados.add(0); // Cualquiera
		
		for(int i=0; i<grafo.vertices()-1; ++i){
			Arista a = menorArista(grafo, visitados); // De un amarillo a un negro
			GRAFO.agregarArista(a.origen, a.destino, a.peso);
			visitados.add(a.destino);
		}
		
		return GRAFO;
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
	
	

	
}









