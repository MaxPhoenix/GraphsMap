package gui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import java.util.ArrayList;

public class Arista {
    private Coordinate a, b;


    public Arista(Coordinate a, Coordinate b) {
        if (a == null || b == null || a.equals(b))
            throw new RuntimeException("Null cordinate");

        this.a = a;
        this.b = b;

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
        ArrayList<Coordinate> coordenadas2 = new ArrayList<Coordinate>();
        coordenadas2.add(a);
        coordenadas2.add(b);
        coordenadas2.add(a);
        coordenadas2.add(b);

        MapPolygon polygon = new MapPolygonImpl(coordenadas2);
        miMapa.addMapPolygon(polygon);

    }

}
