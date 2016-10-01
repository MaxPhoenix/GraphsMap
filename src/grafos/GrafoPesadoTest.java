package grafos;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrafoPesadoTest
{
	@Test
	public void contieneAristaTest()
	{
		GrafoPesado grafo = instancia();
		
		assertTrue( grafo.contieneArista(0, 1) );
		assertTrue( grafo.contieneArista(0, 3) );
		assertFalse( grafo.contieneArista(1, 3) );
		assertFalse( grafo.contieneArista(2, 0) );
	}

	@Test
	public void pesosTest()
	{
		GrafoPesado grafo = instancia();
		
		assertEquals( 13, grafo.getPeso(0, 1), 1e-4 );
		assertEquals( 10, grafo.getPeso(3, 0), 1e-4 );
		assertEquals( 11, grafo.getPeso(3, 2), 1e-4 );
		assertEquals( 12, grafo.getPeso(1, 2), 1e-4 );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void pesoFallidoTest()
	{
		GrafoPesado grafo = instancia();
		grafo.getPeso(0, 2);
	}

	private GrafoPesado instancia()
	{
		GrafoPesado grafo = new GrafoPesado(4);

		grafo.agregarArista(0, 1, 13);
		grafo.agregarArista(1, 2, 12);
		grafo.agregarArista(3, 0, 10);
		grafo.agregarArista(3, 2, 11);
		
		return grafo;
	}
}
