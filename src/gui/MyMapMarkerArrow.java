package gui;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class MyMapMarkerArrow extends MapPolygonImpl {

    public MyMapMarkerArrow(List<? extends ICoordinate> points) {
            super(null, null, points);
    }

    @Override
    public void paint(Graphics g, List<Point> points) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getColor());
        g2d.setStroke(getStroke());
        Path2D path = buildPath(points);
        g2d.draw(path);
        g2d.dispose();
    }

    private Path2D buildPath(List<Point> points) {
        Path2D path = new Path2D.Double();
        if (points != null && points.size() > 0) {
            Point firstPoint = points.get(0);
            path.moveTo(firstPoint.getX(), firstPoint.getY());
            for (Point p : points) {
                path.lineTo(p.getX(), p.getY());
            }

            int pointsSize = points.size() - 1;

            if (points.get(0).getY() > points.get(1).getY()) {
                path.lineTo(points.get(pointsSize).getX(),
                        points.get(pointsSize).getY() + 20);

                path.moveTo(points.get(pointsSize).getX(),
                        points.get(pointsSize).getY());

                path.lineTo(points.get(pointsSize).getX() - 20,
                        points.get(pointsSize).getY());
            } else {
                path.lineTo(points.get(pointsSize).getX(),
                        points.get(pointsSize).getY() - 20);

                path.moveTo(points.get(pointsSize).getX(),
                        points.get(pointsSize).getY());

                path.lineTo(points.get(pointsSize).getX() + 20,
                        points.get(pointsSize).getY());
            }
        }
        return path;
    }
}