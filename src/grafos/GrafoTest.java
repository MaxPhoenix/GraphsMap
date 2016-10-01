package grafos;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class GrafoTest
{
	@Test
	public void agregarAristaTest()
	{
		Grafo grafo = caminito();
		
		assertTrue( grafo.contieneArista(2, 0) );
		assertTrue( grafo.contieneArista(0, 2) );
		assertTrue( grafo.contieneArista(1, 2) );
		assertFalse( grafo.contieneArista(2, 3) );
		assertFalse( grafo.contieneArista(1, 4) );
	}
	//dd
	@Test
	public void agregarAristaBordeTest()
	{
		Grafo grafo = caminito();
		grafo.agregarArista(0, 4);
		grafo.agregarArista(2, 4);
		
		assertTrue( grafo.contieneArista(2, 0) );
		assertTrue( grafo.contieneArista(0, 2) );
		assertTrue( grafo.contieneArista(0, 4) );
		assertTrue( grafo.contieneArista(4, 0) );
		assertTrue( grafo.contieneArista(2, 4) );
		assertTrue( grafo.contieneArista(4, 2) );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void agregarAristaIlegalTest()
	{
		Grafo grafo = caminito();
		grafo.agregarArista(-1, 2);
	}

	@Test
	public void removerAristaTest()
	{
		Grafo grafo = caminito();
		grafo.removerArista(2, 0);
		
		assertFalse( grafo.contieneArista(2, 0) );
		assertTrue( grafo.contieneArista(2, 1) );
		assertFalse( grafo.contieneArista(0, 2) );
	}

	@Test
	public void removerAristaRepetidaTest()
	{
		Grafo grafo = caminito();
		grafo.removerArista(2, 0);
		grafo.removerArista(2, 0);
		grafo.removerArista(2, 0);
		
		assertFalse( grafo.contieneArista(2, 0) );
		assertTrue( grafo.contieneArista(2, 1) );
		assertFalse( grafo.contieneArista(0, 2) );
	}
	
	@Test
	public void vecinosTest()
	{
		Grafo grafo = casita();
		Set<Integer> vecinos = grafo.vecinos(3);
			
		assertEquals(3, vecinos.size());
		assertTrue(vecinos.contains(1));
		assertTrue(vecinos.contains(2));
		assertTrue(vecinos.contains(4));
	}
	
	@Test
	public void parametrosTest()
	{
		Grafo grafo = caminito();
		
		assertEquals(5, grafo.vertices());
		assertEquals(2, grafo.aristas());		
		
		grafo.removerArista(2, 0);
		
		assertEquals(5, grafo.vertices());
		assertEquals(1, grafo.aristas());		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void loopsTest()
	{
		Grafo grafo = caminito();
		grafo.agregarArista(3, 3);
	}	

	private Grafo caminito()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(0, 2);
		grafo.agregarArista(1, 2);
	
		return grafo;
	}

	private Grafo casita()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(0, 1);
		grafo.agregarArista(0, 2);
		grafo.agregarArista(1, 3);
		grafo.agregarArista(2, 3);
		grafo.agregarArista(3, 4);
		grafo.agregarArista(2, 4);
		
		return grafo;
	}
}
