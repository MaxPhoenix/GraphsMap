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
    public ArrayList<Coordinate> coordenadas = new ArrayList<>();
    FileManager f;
    private ArrayList<Arista> aristasOriginal = new ArrayList<>();

    public ArrayList<Coordinate> getCoordenadas() {
        return coordenadas;
    }

    public ArrayList<Arista> getAristas() {
        return aristas;
    }

    public GrafoPesado getGp() {
        return gp;
    }

    private ArrayList<Arista> aristas;
    private GrafoPesado gp;
    
    public GrafoJmap(FileManager f) {
        this.f = f;
        this.coordenadas = f.getCordinates();
     //----------------------------------------------------------------------------------------
        //ESTO CRASHEA SI EL ARCHIVO ESTA VACIO, NO ES ESA LA IDEA, ES LA FUNCION CLUSTERS EL TEMA CUANDO RECIBE UN ARCHIVO VACIO.
        // LA IDEA ORIGINAL DEL TP ES QUE NO SE HAGAN CLUSTERS NI BIEN SE ABRE EL PROGRAMA, SINO DESPUES
        //LO QUE PUSIMOS ACA ES DE PRUEBA NOMAS 
        gp = toGrafo(coordenadas);
        gp = Algoritmos.AGM(gp);
       // gp = Algoritmos.Clusters(gp);
        toArista(gp);
        aristasOriginal = aristas;
       //----------------------------------------------------------------------------------
    }

    public GrafoJmap(FileManager f,boolean test) {
        this.f = f;
        this.coordenadas = f.getCordinates();

        //----------------------------------------------------------------------------------------
        //ESTO CRASHEA SI EL ARCHIVO ESTA VACIO, NO ES ESA LA IDEA, ES LA FUNCION CLUSTERS EL TEMA CUANDO RECIBE UN ARCHIVO VACIO.
        // LA IDEA ORIGINAL DEL TP ES QUE NO SE HAGAN CLUSTERS NI BIEN SE ABRE EL PROGRAMA, SINO DESPUES
        //LO QUE PUSIMOS ACA ES DE PRUEBA NOMAS

        gp = toGrafo(coordenadas);
      if(!test) {


          gp = Algoritmos.AGM(gp);
          // gp = Algoritmos.Clusters(gp);
          toArista(gp);
          aristasOriginal = aristas;
          //----------------------------------------------------------------------------------
      }
    }

    public static float distFrom(Coordinate cor1, Coordinate cor2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(cor2.getLat() - cor1.getLat());
        double dLng = Math.toRadians(cor2.getLat() - cor1.getLat());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(cor1.getLat())) * Math.cos(Math.toRadians(cor2.getLat())) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    public void render(JMapViewer miMapa) {
        Color color = Color.RED;

        for (Coordinate c : coordenadas) {
            MapMarker nuevoMarker = new MapMarkerDot(c);
            nuevoMarker.getStyle().setBackColor(color);
            miMapa.addMapMarker(nuevoMarker);
        }
        for (Arista v : aristas)
            v.render(miMapa);
    }

    public void toArista(GrafoPesado gp) {
        ArrayList<Arista> ret = new ArrayList<>(gp.vertices());

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

        this.aristas=ret;
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



}