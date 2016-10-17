package gui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class Arista {
    private Coordinate a, b;


    public Arista(Coordinate a, Coordinate b) {
        if (a == null || b == null )
            throw new RuntimeException("Null cordinate");

        this.a = a;
        this.b = b;

    }

    public static Arista getMax(ArrayList <Arista> list){
    	
    	Coordinate cor=new Coordinate(1,1);
    	Arista max=new Arista(cor,cor);
    
    	Arista aux=null;
    	for (Arista arista: list){
    		if(arista.getPeso ()>max.getPeso()){
    			aux=arista;
    			max=arista;
    		}
    	}
    	return aux;
    }

    //un arreglo con todas las distancias del grafo
    public static ArrayList<Double> distances(ArrayList<Arista> list){
        ArrayList<Double> distances = new ArrayList<> (list.size ());
        distances.addAll (list.stream ().map (Arista::getPeso).collect (Collectors.toList ()));
        Collections.sort(distances);
        return distances;
    }

    // busca un promedio estimativo entre las distancias
    public static Double promedio(ArrayList<Double> list){
        Double promedio;
        Double suma = 0.0;
        double cant = list.size();
        for(Double d : list){
            suma += d;
        }
        promedio = suma/cant;
        return promedio;
    }

    // busca el elemento en el arreglo de distancias mas cercano al promedio (medium)
    private static Double mediumDistance(ArrayList<Double> doublesList, Double medium, int start, int finish){

       // ejemplo [1,2,3,4,5,6,7,8,9,10] promedio = 5.5;

        if(start  == finish || start>finish) //si se encuentran
            return doublesList.get(start);

        if (Objects.equals (doublesList.get (start), medium) || Objects.equals (doublesList.get (finish), medium)) //el promedio esta en la lista
            return medium;

        if(doublesList.get(start) < medium && doublesList.get(finish) > medium) //si esta entre los valores me acerco al medio
            return mediumDistance(doublesList,medium,start+1,finish-1);

        if(doublesList.get(start) < medium && !(doublesList.get(finish) > medium)) //si es mayor al de inicio y al del final voy hacia el final
            return mediumDistance(doublesList,medium,start+1,finish);

        return mediumDistance(doublesList,medium,start+1,finish); //lo contrario a lo anterior
    }

    // metodo wrapper del anterior
    public static Double mediumDistance(ArrayList<Double> list, Double promedio){
        return mediumDistance(list,promedio, 0, list.size()-1);
    }
    //una vez obtenido el promedio busca la arista correspondiente a esa distancia
    public static Arista mediumArista(ArrayList<Arista> list, Double distance){
        for(Arista arista: list){
            if(arista.getPeso() == distance)
                return arista;
        }
        return null;
    }

    public double getPeso (){
    	return GrafoJmap.distFrom(this.a,this.b);
    	
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (obj instanceof Arista) {
            Arista ar = (Arista) obj;
            boolean AyBsoniguales = this.a.equals(ar.a) && this.b.equals(ar.b);
            boolean AyBsonSimilares = this.a.equals(ar.b) && this.b.equals(ar.a);

            if (AyBsoniguales || AyBsonSimilares)
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }

    public void render(JMapViewer miMapa) {
        ArrayList<Coordinate> coordenadas2 = new ArrayList<> ();
        coordenadas2.add(a);
        coordenadas2.add(b);
        coordenadas2.add(a);

        MapPolygon polygon = new MapPolygonImpl(coordenadas2);

        miMapa.addMapPolygon(polygon);

    }

}
