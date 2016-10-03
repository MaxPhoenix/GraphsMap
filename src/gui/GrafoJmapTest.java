package gui;

import org.junit.Test;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import static gui.GrafoJmap.distFrom;
import static org.junit.Assert.assertEquals;

/**
 * Created by Agus on 10/3/2016.
 */
public class GrafoJmapTest {
    @Test
    public void distFromTest(){
        Coordinate cor1=new Coordinate (-34.52126711205503,-58.70325565338135);
        Coordinate cor2=new Coordinate (-34.52186820666683,-58.70265483856201);
        //84.04mts segun Google
        assertEquals(84.04,distFrom(cor1,cor2),10);
    }

    @Test
    public void toAristaTest() {
        GrafoJmap test=instancia(true);
        GrafoJmap bkp=instancia(false);
        test.toArista(bkp.getGp());

        System.out.println("Aristas= "+test.getAristas().size()+" | "+bkp.getGp().aristas());
        if( test.f.nombre.equals("Archivos/instanciaTest.json"))
            assertEquals(3, test.getAristas().size());


    }
@Test
    public void toGrafoTest() {
        GrafoJmap bkp=instancia(true);
        GrafoJmap test=instancia(false);
        String ret1= "Vertice: 0 Vecinos: [1, 2, 3]"+"\n"+
                     "Vertice: 1 Vecinos: [0, 2, 3]"+"\n"+
                     "Vertice: 2 Vecinos: [0, 1, 3]"+"\n"+
                     "Vertice: 3 Vecinos: [0, 1, 2]"+"\n";

        String ret2= "Vertice: 0 Vecinos: [2, 3]"+"\n"+
                     "Vertice: 1 Vecinos: [2]"+"\n"+
                     "Vertice: 2 Vecinos: [0, 1]"+"\n"+
                     "Vertice: 3 Vecinos: [0]"+"\n";


    assertEquals(ret1,bkp.getGp().toString());
    assertEquals(ret2,test.getGp().toString());





    }


    public GrafoJmap instancia(boolean incializada){
        FileManager f=new FileManager("Archivos/instanciaTest.json");
        GrafoJmap gp=new GrafoJmap(f,incializada);
        return gp;
    }




}
