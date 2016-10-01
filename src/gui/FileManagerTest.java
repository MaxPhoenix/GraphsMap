package gui;

import org.junit.Ignore;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import java.util.ArrayList;

/**
 * Created by Max on 9/29/2016.
 */
public class FileManagerTest {


    public static void retrieveCoordinates() {
        FileManager m = new FileManager("prueba.txt");
        m.getCordinates().forEach(System.out::println);
    }

    @Ignore
    public void storeCoordinates() throws Exception {
        FileManager m = manager();
        m.storeCoordinates("prueba.txt");

    }

    private FileManager manager(){
        FileManager m = new FileManager("prueba.txt");
        ArrayList<Coordinate> coordenadas= coordenadas();
       
        for(Coordinate c: coordenadas)
            m.getCordinates().add(c);
        return m;
    }

    //prieba de coordinate para guardar
    private ArrayList<Coordinate> coordenadas(){
        ArrayList<Coordinate> coordenadas = new ArrayList<Coordinate>();
        coordenadas.add(new Coordinate(-34.532, -58.7128));
        coordenadas.add(new Coordinate(-34.546, -58.719));
        coordenadas.add(new Coordinate(-34.559, -58.721));
        coordenadas.add(new Coordinate(-34.569, -58.725));
        coordenadas.add(new Coordinate(-34.532, -58.730));
        return coordenadas;
    }
    
}