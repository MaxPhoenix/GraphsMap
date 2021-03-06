package gui;


import grafos.Algoritmos;
import grafos.GrafoPesado;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

import static gui.GrafoJmap.GraphType.CAMINOGOLOSO;


/**
 * Created by Agus on 28/9/2016.
 */
@SuppressWarnings("ALL")
public class GrafoJmap extends Thread{
     FileManager f;

    private ArrayList<Coordinate> coordenadas = new ArrayList<>();
    private ArrayList<AristaGrafica> aristasCaminoMinimo = new ArrayList<>();
    private ArrayList<AristaGrafica> aristasAGM = new ArrayList<>();
    private ArrayList<AristaGrafica> aristasCompleto = new ArrayList<>();
    private ArrayList<AristaGrafica> aristasClusters = new ArrayList<>();
    private ArrayList<AristaGrafica> aristasActuales = new ArrayList<AristaGrafica>();  //este swapea entre las distinas opciones de grafo
    private GrafoPesado grafoCompleto;
    private GrafoPesado AGM,caminoMinimo;
    private JMapViewer miMapa;
    private Double aristas=0D;
    private int cantClusters=0;
    private Menu menu;
    private boolean Loaded;


    public enum GraphType{
        AGM, CAMINOGOLOSO,COMPLETO,CLUSTERS,NINGUNA;

        public String toString(){
    	    switch(this){
    	    case AGM:return "AGM";
    	    case COMPLETO: return "Completo";
    	    case CLUSTERS: return "Clusters";
    	    case NINGUNA: return "Ninguna";
            case CAMINOGOLOSO: return "CAMINOGOLOSO";
    	    default: return "Desconocido";
    	    }
        }
    }

    public enum Cluster{MAXIMO,PROMEDIO,INTELIGENTE}

    public GrafoPesado getGrafoCompleto() {
        return grafoCompleto;
    }

    public ArrayList<AristaGrafica> getAristasAGM() {
        return aristasAGM;
    }

    public String getCantClusters() {
        return Integer.toString(cantClusters);
    }

    public ArrayList<Coordinate> getCoordenadas (){ return coordenadas;}

    public boolean isLoaded(){ return Loaded;}


    public GrafoJmap(FileManager f, Menu men) {
        this.f = f;
        this.miMapa= Menu.getMiMapa ();
        this.menu=men;
        this.coordenadas = f.getCordinates();
        this.start();

    }

    public GrafoJmap(ArrayList<Coordinate> bkp, Menu men) {
        this.coordenadas=bkp;
        this.miMapa= Menu.getMiMapa ();
        this.menu=men;
        this.start();
    }

    public GrafoJmap(FileManager f) {
        this.f = f;
        this.coordenadas = f.getCordinates();
        this.start();
    }

    @SuppressWarnings("unchecked")
	public void run(){
        Loaded = false;
        menu.setProgress("Cargando Coordenadas",0);

        if(!isInterrupted())
            grafoCompleto = toGrafo(coordenadas);
        if(!isInterrupted())
            aristasCompleto=toArista(grafoCompleto);

        if(!isInterrupted())
            AGM = Algoritmos.AGM(grafoCompleto,menu);
        if(!isInterrupted())
            caminoMinimo=Algoritmos.CaminoMinimo(grafoCompleto,menu);

        if(!isInterrupted())
            aristasCaminoMinimo=toArista(caminoMinimo);
        if(!isInterrupted())
            aristasAGM = toArista(AGM);
        else{
            return;
        }

        aristasClusters = (ArrayList<AristaGrafica>) aristasAGM.clone();
        Loaded = true;

        menu.setProgress("Completado",100);
        this.interrupt();

    }


    public static double distFrom(Coordinate cor1, Coordinate cor2) {
        double ret = 0.0;
        if(cor1.equals(cor2))
            return ret;
        else if (cor2 == null)
            return ret;
        double lat1 = cor1.getLat();
        double lat2 = cor2.getLat();
        double lon1 = cor1.getLon();
        double lon2 = cor2.getLon();
        ret = Math.sqrt( ((lat2-lat1)*(lat2-lat1)) + ((lon2-lon1)*(lon2-lon1)) ) ;
        return ret;
    }


    public void render() {
        if(coordenadas!=null) {
            Color color;
            for (int i = 0; i < coordenadas.size() ; i++) {
                if(i>0) {color=Color.RED;}
                else {color=Color.GREEN;}
                Coordinate actual = coordenadas.get(i);
                MapMarkerDot nuevoMarker = new MapMarkerDot(actual);
                nuevoMarker.getStyle().setBackColor(color);
                miMapa.addMapMarker(nuevoMarker);
            }
        }
        for (AristaGrafica v : this.aristasActuales)
            v.render(miMapa);
    }

    private ArrayList<AristaGrafica> toArista(GrafoPesado gp) {
        ArrayList<AristaGrafica> ret = new ArrayList<AristaGrafica>(gp.vertices());

        for (int i = 0; i < gp.vertices(); i++) {
            Set<Integer> vecinos = gp.vecinos(i);
            Coordinate cor1 = coordenadas.get(i);

            for (Integer j : vecinos) {
                if(isInterrupted()){
                    this.interrupt();
                    return new ArrayList<AristaGrafica>();
                }
                Coordinate cor2 = coordenadas.get(j);
                AristaGrafica aristaGrafica = new AristaGrafica (cor1, cor2);
                if (!ret.contains(aristaGrafica)) {
                    ret.add(aristaGrafica);
                    aristas++;
                }
            }

            menu.setProgress("Generando Aristas...",(i*100)/gp.vertices());
        }
          
        return ret;
    }



    private GrafoPesado toGrafo(ArrayList<Coordinate> list) {
        GrafoPesado grafo = new GrafoPesado(list.size());
        for (int i = 0; i < list.size(); i++) {
            for (int j = i; j < list.size(); j++) {
                if (i != j)
                    grafo.agregarArista(i, j, distFrom(list.get(i), list.get(j)));
            }
            menu.setProgress("Cargando Grafo...",(i*100)/list.size());
        }
        return grafo;
    }

    public ArrayList<AristaGrafica> getAristasActuales() {
        return aristasActuales;
    }

    public void changeClusterMode(Cluster cluster , int cantClusters) throws  IllegalArgumentException{

        if(cantClusters > aristasAGM.size())
            throw new IllegalArgumentException("argumento mayor a la cant aristas");

        if(menu.Modo== CAMINOGOLOSO)
            aristasClusters = (ArrayList<AristaGrafica>) aristasCaminoMinimo.clone();

        else{
            aristasClusters = (ArrayList<AristaGrafica>) aristasAGM.clone();
        }

        if(cluster==Cluster.MAXIMO){
            for( int i=0 ; i<cantClusters-1; i++){
              aristasClusters.remove((AristaGrafica.getMax(aristasClusters)));
            }
            System.out.println("Cluster "+cluster);
        }

        ArrayList<Double> distances = AristaGrafica.distances(aristasClusters); //ya ordenada
        Double promedio = AristaGrafica.promedio(distances);
        if(cluster==Cluster.PROMEDIO) {
            for (int i = 0; i < cantClusters - 1; i++) {
                promedio = AristaGrafica.promedio(distances);
                Double mediumDistance = AristaGrafica.mediumDistance(distances, promedio);
                aristasClusters.remove(AristaGrafica.mediumArista(aristasClusters, mediumDistance));
            }
            System.out.println("Cluster " + cluster);
        }

        this.cantClusters=cantClusters;
        if(cluster==Cluster.INTELIGENTE){
            Double Peso=0D;
            for(AristaGrafica ar: aristasClusters){
                Peso+=ar.getPeso();
            }
            int i=0;
            while(promedio*1.04*(aristasClusters.size()-1)>Peso){
                i++;

                aristasClusters.remove((AristaGrafica.getMax(aristasClusters)));
            }
            this.cantClusters=i;
        }

        aristasActuales=aristasClusters;
        menu.setProgress("Completado",100);
    }

    //este metodo cambia el modo de grafo entre completo, agm y clusters


    public void changeMode(GraphType modo){

        if(modo == GraphType.AGM) {
           this.aristasActuales= aristasAGM;

        }
        else if (modo == GraphType.CLUSTERS) {
        	 this.aristasActuales= aristasClusters;
        }

        else if(modo == CAMINOGOLOSO){
            this.aristasActuales=aristasCaminoMinimo;
            System.out.println("camino.");
        }
        else if(modo == GraphType.COMPLETO) {
        	 this.aristasActuales= aristasCompleto;

        }
        else if(modo == GraphType.NINGUNA)
        	this.aristasActuales=new ArrayList<>();


        System.out.println("Cambio de modo a " + modo );

            this.render();

    }


}