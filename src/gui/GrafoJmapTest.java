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
        assertEquals(8.4,distFrom(cor1,cor2),10);
    }

    @Test
    public void toAristaTest() {
        GrafoJmap test=instancia(true);
        GrafoJmap bkp=instancia(false);

        test.toArista(bkp.getGrafoCompleto());

        System.out.println("Aristas= "+test.getAristasActuales().size()+" | "+bkp.getGrafoCompleto().aristas());
        if( test.f.nombre.equals("Archivos/instanciaTest.json"))
            assertEquals(3, test.getAristasActuales().size());


    }
@Test
    public void toGrafoTest() {
        GrafoJmap bkp=instancia(true);
        GrafoJmap test=instancia(false);
        String ret1= "Vertice: 0 Vecinos: [1, 2, 3]"+"\n"+
                     "Vertice: 1 Vecinos: [0, 2, 3]"+"\n"+
                     "Vertice: 2 Vecinos: [0, 1, 3]"+"\n"+
                     "Vertice: 3 Vecinos: [0, 1, 2]"+"\n";

        String ret2= "Vertice: 0 Vecinos: [1]"+"\n"+
                     "Vertice: 1 Vecinos: [0, 2]"+"\n"+
                     "Vertice: 2 Vecinos: [1, 3]"+"\n"+
                     "Vertice: 3 Vecinos: [2]"+"\n";


    assertEquals(ret1,bkp.getGrafoCompleto().toString());
    assertEquals(ret2,test.getGrafoCompleto().toString());





    }


    public GrafoJmap instancia(boolean incializada){
        FileManager f=new FileManager("Archivos/instanciaTest.json");
        f.retrieveCoordinates();
        GrafoJmap gp=new GrafoJmap(f,incializada);
        return gp;
    }




}
