package gui;

import org.junit.Test;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Max on 9/29/2016.
 */
public class FileManagerTest {


    @Test
    public void retrieveCoordinates() {
        FileManager m = new FileManager("Archivos/instanciaTest.json");
        m.retrieveCoordinates();
        String test="";
        String bkp="Coordinate[-34.52133782929332, -58.70068073272705]"+
        "Coordinate[-34.520772089706036, -58.702311515808105]"+
        "Coordinate[-34.52126711205503, -58.70325565338135]"+
        "Coordinate[-34.52186820666683, -58.70265483856201]";
        for (Coordinate cor: m.getCordinates()){
            test+=cor;
        }
        assertEquals(bkp,test);


    }

    @Test
    public void storeCoordinates() throws Exception {
        ArrayList<Coordinate> coordenadas = new ArrayList<Coordinate>();
        coordenadas.add(new Coordinate(-34.532, -58.7128));
        coordenadas.add(new Coordinate(-34.546, -58.719));
        coordenadas.add(new Coordinate(-34.559, -58.721));
        coordenadas.add(new Coordinate(-34.569, -58.725));

        FileManager m = new FileManager("Archivos/test.json");
        m.setCor(coordenadas);
        m.storeCoordinates("Archivos","test.json");

        m = new FileManager("Archivos/test.json");
        ArrayList<Coordinate> test=m.retrieveCoordinates("Archivos/test.json");

        assertEquals(coordenadas.toString(),test.toString());


    }

    @Test
    public void delete() {

        try {
            File file = new File("Archivos/test.json");
            file.delete();
        } catch (Exception e) {
            System.err.format("%s: no such" + " file or directory");
        }

        File file = new File("Archivos/test.json");
        assertFalse(file.exists());
    }

    
}