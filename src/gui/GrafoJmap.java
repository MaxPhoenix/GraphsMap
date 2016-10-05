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

    private ArrayList<Arista> aristasAGM = new ArrayList<>();
    private ArrayList<Arista> aristasCompleto = new ArrayList<>();
    private ArrayList<Arista> aristasClusters = new ArrayList<>();
    private ArrayList<Arista> drawableAristas = new ArrayList<Arista>();  //este swapea entre las distinas opciones de grafo

    private GrafoPesado grafoCompleto;
    private GrafoPesado AGM;
    private GrafoPesado clusters;
    private GrafoPesado drawableGraph ;

    public enum GraphType{AGM,COMPLETO,CLUSTERS}

    private String graphMode = "Completo";

    private ArrayList<Coordinate> getCoordenadas() {
        return coordenadas;
    }
    public ArrayList<Arista> getDrawableAristas() {
        return drawableAristas;
    }
    public GrafoPesado getGrafoCompleto() {
        return grafoCompleto;
    }
    public GrafoPesado getAGM() {
        return AGM;
    }
    public GrafoPesado getClusters() {
        return clusters;
    }
    public String getMode(){return this.graphMode;}

//Constructor con todos los tipos de grafos creados con sus respectivas aristas pereparadas para su uso de ser necesario
    public GrafoJmap(FileManager f) {
        this.f = f;
        this.coordenadas = f.getCordinates();

        grafoCompleto = toGrafo(coordenadas);
        AGM = Algoritmos.AGM(grafoCompleto);
        GrafoPesado agmAux = Algoritmos.AGM(grafoCompleto); //este es para que agm no se modifique al hacer clusters y se dibuje completo
        clusters = Algoritmos.Clusters(agmAux);

        aristasCompleto = toArista(grafoCompleto);
        aristasAGM = toArista(AGM);
        aristasClusters = toArista(clusters);

        this.drawableGraph = AGM;
        this.drawableAristas = toArista(drawableGraph);

    }

    //TODO este lo usaste para algun test tuyo agus, fijate si lo dejas o no
    public GrafoJmap(FileManager f,boolean test) {
        this.f = f;
        this.coordenadas = f.getCordinates();

        //----------------------------------------------------------------------------------------
        //ESTO CRASHEA SI EL ARCHIVO ESTA VACIO, NO ES ESA LA IDEA, ES LA FUNCION CLUSTERS EL TEMA CUANDO RECIBE UN ARCHIVO VACIO.
        // LA IDEA ORIGINAL DEL TP ES QUE NO SE HAGAN CLUSTERS NI BIEN SE ABRE EL PROGRAMA, SINO DESPUES
        //LO QUE PUSIMOS ACA ES DE PRUEBA NOMAS

        grafoCompleto = toGrafo(coordenadas);

      if(!test) {


          grafoCompleto = Algoritmos.AGM(grafoCompleto);
          // grafoCompleto = Algoritmos.Clusters(grafoCompleto);
          this.drawableAristas = toArista(grafoCompleto);

          //----------------------------------------------------------------------------------
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
        this.drawableAristas = toArista(this.drawableGraph);

        Color color = Color.RED;

        for (Coordinate c : coordenadas) {
            MapMarker nuevoMarker = new MapMarkerDot(c);
            nuevoMarker.getStyle().setBackColor(color);
            miMapa.addMapMarker(nuevoMarker);
        }
        for (Arista v : drawableAristas)
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

//este metodo cambia el modo de grafo entre completo, agm y clusters
    public void changeMode(GraphType modo){
        if(modo == GraphType.AGM) {
            this.drawableGraph = AGM;
            this.graphMode = "AGM";
        }
        else if (modo == GraphType.CLUSTERS) {
            this.drawableGraph = clusters;
            this.graphMode = "Clusters";
        }
        else if(modo == GraphType.COMPLETO) {
            this.drawableGraph = grafoCompleto;
            this.graphMode = "Completo";
        }
        else
            return;
    }

}