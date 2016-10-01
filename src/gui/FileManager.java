package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Agus on 28/9/2016.
 */

public class FileManager implements Serializable {
    String nombre;
    private ArrayList<Coordinate> cor = new ArrayList<>();
    
    
    public FileManager(String filename) {
    	nombre=filename;
        try {
            cor = retrieveCoordinates(filename);
        } catch (Exception e) {
            cor = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public ArrayList<Coordinate> getCordinates() {
        return cor;
    }

    public ArrayList<Coordinate> retrieveCoordinates(String fileName) throws Exception {
        Gson gson = new Gson();
        ArrayList<Coordinate> coordenadas = new ArrayList<>();
        File f = new File(fileName);
        try{
            if(f.exists() == true) {
                Type tipoCoordenada = new TypeToken<List<Coordenada>>() {}.getType();
                List<Coordenada> coordenada = gson.fromJson(new FileReader(fileName), tipoCoordenada);
                if (coordenada == null)
                   System.out.println("Archivo vacio, creando uno nuevo...");
                else
                	for (Coordenada c : coordenada)
                		coordenadas.add(new Coordinate(c.getLat(), c.getLon()));
            }
            else{
            	//ESTO ES PARA QUE, SI EL ARCHIVO NO EXISTE, SE CREA UNO VACIO, QUE DESPUES SE MODIFICA SEGUN LO QUE GUARDE EL USUARIO
            	BufferedWriter br = new BufferedWriter (new FileWriter(fileName));
            	br.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("No se encuentra el archivo especificado");
        }

        return coordenadas;
    }

    //convierte un arreglo de Coordinate a uno de Coordenada y lo guarda
    public void storeCoordinates() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Coordenada> storage = new ArrayList<Coordenada>();

        //conversion de coordinate a coordaenada
        for(Coordinate c : this.cor)
            storage.add(new Coordenada(c.getLat(),c.getLon()));

        String coordenadas = gson.toJson(storage);  //guardado de coordenada

        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(nombre));
            fileWriter.write(coordenadas);
            fileWriter.close();
        }
        catch (Exception e){
            System.out.println("Falla en la escritura de archivos");
        }

    }
    public void storeCoordinates(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Coordenada> storage = new ArrayList<Coordenada>();

        //conversion de coordinate a coordaenada
        for(Coordinate c : this.cor)
            storage.add(new Coordenada(c.getLat(),c.getLon()));

        String coordenadas = gson.toJson(storage);  //guardado de coordenada

        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename));
            fileWriter.write(coordenadas);
            fileWriter.close();
        }
        catch (Exception e){
            System.out.println("Falla en la escritura de archivos");
        }

    }
}