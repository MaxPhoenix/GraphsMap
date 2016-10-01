package gui;

import java.io.Serializable;

/**
 * Created by Agus on 30/9/2016.
 */
class Coordenada implements Serializable {
	private static  final long serialVersionUID = 1L;
    double latitud, longitud;

    public Coordenada(double lat, double lon) {
        this.latitud = lat;
        this.longitud = lon;
    }

    public double getLat() {
        return this.latitud;
    }

    public double getLon() {
        return this.longitud;
    }

}