package gui;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agus on 28/9/2016.
 */

public class FileManager implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nombre;
	boolean archivoCorrupto=false;
    private ArrayList<Coordinate> cor = new ArrayList<>();


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Coordinate> getCor() {
        return cor;
    }


    public void setCor(ArrayList<Coordinate> cor) {
        this.cor = cor;
    }

    public FileManager(String fileName) {

        nombre = fileName;
    }

    public ArrayList<Coordinate> retrieveCoordinates(String fileName) {
        Gson gson = new Gson();
        ArrayList<Coordinate> coordenadas = new ArrayList<>();
        File f = new File(fileName);
        try{
            if(f.exists() == true ) {
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
        cor=coordenadas;
        return coordenadas;
    }


    public ArrayList<Coordinate> retrieveCoordinates() {
        Gson gson = new Gson();
        ArrayList<Coordinate> coordenadas = new ArrayList<>();
        File f = new File(this.nombre);
        try{
            if(f.exists() == true ) {
                Type tipoCoordenada = new TypeToken<List<Coordenada>>() {}.getType();
                List<Coordenada> coordenada;
                try{
                coordenada = gson.fromJson(new FileReader(this.nombre), tipoCoordenada);}
                catch(Exception e){
                	 System.out.println("Archivo corrupto, generando instancia vacia");
                	  archivoCorrupto=true;
                	return coordenadas;
                	
                }
                if (coordenada == null)
                    System.out.println("Archivo vacio, creando uno nuevo...");
     
                
                else
                    for (Coordenada c : coordenada)
                        coordenadas.add(new Coordinate(c.getLat(), c.getLon()));
            }
            else{
                //ESTO ES PARA QUE, SI EL ARCHIVO NO EXISTE, SE CREA UNO VACIO, QUE DESPUES SE MODIFICA SEGUN LO QUE GUARDE EL USUARIO
                BufferedWriter br = new BufferedWriter (new FileWriter(this.nombre));
                br.close();
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("No se encuentra el archivo especificado");
        }
        cor=coordenadas;
        return coordenadas;

    }


    public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isArchivoCorrupto() {
		return archivoCorrupto;
	}

	public void storeCoordinates(String dirName,String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Coordenada> storage = new ArrayList<Coordenada>();

        //conversion de coordinate a coordaenada
        for(Coordinate c : this.cor)
            storage.add(new Coordenada(c.getLat(),c.getLon()));

        String coordenadas = gson.toJson(storage);  //guardado de coordenada

        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(dirName+File.separator+fileName));
            fileWriter.write(coordenadas);
            fileWriter.close();
        }
        catch (Exception e){
            System.out.println("Falla en la escritura de archivos");
        }

    }
    public void storeCoordinates() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Coordenada> storage = new ArrayList<Coordenada>();

        //conversion de coordinate a coordaenada
        for(Coordinate c : this.cor)
            storage.add(new Coordenada(c.getLat(),c.getLon()));

        String coordenadas = gson.toJson(storage);  //guardado de coordenada

        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(this.nombre));
            fileWriter.write(coordenadas);
            fileWriter.close();
        }
        catch (Exception e){
            System.out.println("Falla en la escritura de archivos");
        }

    }

    public void setCordinates(ArrayList<Coordinate> cordinates) {
        this.cor = cordinates;
    }

    public ArrayList<Coordinate> getCordinates() {
        return cor;
    }
}