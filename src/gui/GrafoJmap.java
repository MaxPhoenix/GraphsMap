package gui;


import grafos.Algoritmos;
import grafos.GrafoPesado;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;


/**
 * Created by Agus on 28/9/2016.
 */
public class GrafoJmap {
     FileManager f;

    public ArrayList<Coordinate> coordenadas = new ArrayList<>();

    public ArrayList<Arista> getAristasAGM() {
        return aristasAGM;
    }

    private ArrayList<Arista> aristasAGM = new ArrayList<>();
    private ArrayList<Arista> aristasCompleto = new ArrayList<>();
    private ArrayList<Arista> aristasClusters = new ArrayList<>();
    private ArrayList<Arista> aristasActuales = new ArrayList<Arista>();  //este swapea entre las distinas opciones de grafo
    private GrafoPesado grafoCompleto;
    private Cluster modoCluster=Cluster.MAXIMO;
    private GrafoPesado AGM;
    private int cantClusters=3;

    public enum GraphType{AGM,COMPLETO,CLUSTERS,NINGUNA;
    @Override	
    public String toString(){
    	switch(this){
    	case AGM:return "AGM";
    	case COMPLETO: return "Completo";
    	case CLUSTERS: return "Clusters";
    	case NINGUNA: return "Ninguna";
    	default: return "Desconocido";
    	}
    }
    }
    public enum Cluster{MAXIMO,PROMEDIO,INTELIGENTE;
    }

    private String graphMode = "Completo";

    private ArrayList<Coordinate> getCoordenadas() {
        return coordenadas;
    }
    public ArrayList<Arista> getDrawableAristas() {
        return aristasActuales;
    }
    public GrafoPesado getGrafoCompleto() {
        return grafoCompleto;
    }
    public GrafoPesado getAGM() {
        return AGM;
    }
   
    public String getMode(){return this.graphMode;}

//Constructor con todos los tipos de grafos creados con sus respectivas aristas pereparadas para su uso de ser necesario
    public GrafoJmap(FileManager f) {
        this.f = f;
        this.coordenadas = f.getCordinates();
        grafoCompleto = toGrafo(coordenadas);
        aristasCompleto=toArista(grafoCompleto);
        AGM = Algoritmos.AGM(grafoCompleto);
        aristasAGM = toArista(AGM);
        aristasClusters = toArista(AGM);
        
       
		
      
    }

    //TODO este lo usaste para algun test tuyo agus, fijate si lo dejas o no
    public GrafoJmap(FileManager f,boolean test) {
        this.f = f;
        this.coordenadas = f.getCordinates();
        grafoCompleto = toGrafo(coordenadas);

      if(!test) {


          grafoCompleto = Algoritmos.AGM(grafoCompleto);
          // grafoCompleto = Algoritmos.Clusters(grafoCompleto);
          this.aristasActuales = toArista(grafoCompleto);


      }
    }


    
    
    public static double distFrom(Coordinate cor1, Coordinate cor2) {
        double ret = 0.0;
        if(cor1.equals(cor2))
            return ret;
        else if (cor1 == null || cor2 == null)
            return ret;
        double lat1 = cor1.getLat();
        double lat2 = cor2.getLat();
        double lon1 = cor1.getLon();
        double lon2 = cor2.getLon();
        ret = Math.sqrt( ((lat2-lat1)*(lat2-lat1)) + ((lon2-lon1)*(lon2-lon1)) ) ;
        return ret;
    }

//ahora render dibuja las aristas en base al modo ya cambiado anteriormente
    public void render(JMapViewer miMapa ) {
        Color color = Color.RED;

        for (Coordinate c : coordenadas) {
            MapMarker nuevoMarker = new MapMarkerDot(c);
            nuevoMarker.getStyle().setBackColor(color);
            miMapa.addMapMarker(nuevoMarker);
        }
        for (Arista v : this.aristasActuales)
            v.render(miMapa);

}

    public ArrayList<Arista> toArista(GrafoPesado gp) {
        ArrayList<Arista> ret = new ArrayList<Arista>(gp.vertices());

        for (int i = 0; i < gp.vertices(); i++) {
            Set<Integer> vecinos = gp.vecinos(i);
            Coordinate cor1 = coordenadas.get(i);

            for (Integer j : vecinos) {
                Coordinate cor2 = coordenadas.get(j);
                Arista arista = new Arista(cor1, cor2);
                if (!ret.contains(arista))
                    ret.add(arista);
            }
        }

        return ret;
    }

    public GrafoPesado toGrafo(ArrayList<Coordinate> list) {
        GrafoPesado grafo = new GrafoPesado(list.size());
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (i != j)
                    grafo.agregarArista(i, j, distFrom(list.get(i), list.get(j)));
            }
        }
        return grafo;

    }

    public ArrayList<Arista> getAristasActuales() {
        return aristasActuales;
    }

    public void changeClusterMode(Cluster cluster , int cantClusters) {
        aristasClusters=toArista(AGM);

        if(cluster==Cluster.MAXIMO){
            for( int i=0 ; i<cantClusters-1; i++){
              aristasClusters.remove((Arista.getMax(aristasClusters)));
            }
            System.out.println("Cluster "+cluster);
        }
        if(cluster==Cluster.PROMEDIO) {
            for (int i = 0; i < cantClusters - 1; i++) {
                ArrayList<Double> distances = Arista.distances(aristasClusters); //ya ordenada
                Double promedio = Arista.promedio(distances);
                Double mediumDistance = Arista.mediumDistance(distances, promedio);
                aristasClusters.remove(Arista.mediumArista(aristasClusters, mediumDistance));

            }
            System.out.println("Cluster " + cluster);
        }


        if(cluster==Cluster.INTELIGENTE){
         //TODO   borrarAristaIntel(this.aristasClusters,cantClusters);
        }

        aristasActuales=aristasClusters;

    }

    //este metodo cambia el modo de grafo entre completo, agm y clusters
    public void changeMode(GraphType modo){

        if(modo == GraphType.AGM) {
           this.aristasActuales= aristasAGM;
           
        }
        else if (modo == GraphType.CLUSTERS) {
        	 this.aristasActuales= aristasClusters;
        }
        else if(modo == GraphType.COMPLETO) {
        	 this.aristasActuales= aristasCompleto;

        }
        else
        	this.aristasActuales=new ArrayList<>();


        System.out.println("Cambio de modo a " + modo );

    }



}