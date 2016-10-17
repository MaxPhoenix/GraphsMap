package gui;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class FileManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String nombre;
	private boolean archivoCorrupto=false;
    private ArrayList<Coordinate> cor = new ArrayList<>();

    public void setCor(ArrayList<Coordinate> cor) {
        this.cor = cor;
    }

    public FileManager(String fileName) {
        nombre = fileName;
    }



    public ArrayList<Coordinate> retrieveCoordinates() {
        Gson gson = new Gson();
        ArrayList<Coordinate> coordenadas = new ArrayList<>();
        File f = new File(this.nombre);
        try{
            if(f.exists()  ) {
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
                    coordenadas.addAll (coordenada.stream ().map (c -> new Coordinate (c.getLat (), c.getLon ())).collect (Collectors.toList ()));
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


    public boolean isArchivoCorrupto() {
		return archivoCorrupto;
	}


    public void storeCoordinates() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Coordenada> storage = new ArrayList<> ();

        //conversion de coordinate a coordaenada
        storage.addAll (this.cor.stream ().map (c -> new Coordenada (c.getLat (), c.getLon ())).collect (Collectors.toList ()));

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