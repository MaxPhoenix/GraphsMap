package grafos;

import gui.Menu;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlgoritmosTest
{
	@Test
	public void unVisitadoTest()
	{
		GrafoPesado grafo = instancia();

		Set<Integer> visitados = new HashSet<> ();
		visitados.add(0);

		Algoritmos.Arista arista = Algoritmos.menorArista(grafo, visitados);
		assertEquals(new Algoritmos.Arista(0, 1, 5), arista);
	}
	
	@Test
	public void tresVisitadosTest()
	{
		GrafoPesado grafo = instancia();

		Set<Integer> visitados = new HashSet<> ();
		visitados.add(0);
		visitados.add(1);
		visitados.add(2);
		
		Algoritmos.Arista arista = Algoritmos.menorArista(grafo, visitados);
		assertEquals(new Algoritmos.Arista(1, 4, 4), arista);
	}

	@Test
	public void primTest()
	{
		GrafoPesado grafo = instancia();
		GrafoPesado agm = Algoritmos.AGM(grafo,new Menu());
		
		assertTrue(agm.contieneArista(1, 2));
		assertTrue(agm.contieneArista(1, 4));
		assertTrue(agm.contieneArista(4 ,3));
		assertTrue(agm.contieneArista(0, 1));
		assertEquals(4, agm.aristas());		
	}
	
	private GrafoPesado instancia()
	{
		GrafoPesado grafo = new GrafoPesado(5);
		grafo.agregarArista(0, 1, 5);
		grafo.agregarArista(0, 2, 6);
		grafo.agregarArista(0, 3, 10);
		grafo.agregarArista(1, 2, 1);
		grafo.agregarArista(2, 3, 5);
		grafo.agregarArista(1, 4, 4);
		grafo.agregarArista(2, 4, 10);
		grafo.agregarArista(3, 4, -10);
		
		return grafo;
	}
	
	
	
	
}
