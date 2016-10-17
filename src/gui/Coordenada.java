package gui;

import java.io.Serializable;
import java.util.Objects;


class Coordenada implements Serializable {
	private static  final long serialVersionUID = 1L;
    private Double latitud;
    private Double longitud;

     Coordenada(){

    }
     Coordenada(double lat, double lon) {
        this.latitud = lat;
        this.longitud = lon;
    }

     Double getLat() {
        return this.latitud;
    }

     Double getLon() {
        return this.longitud;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj instanceof  Coordenada){
            Coordenada cor2 = (Coordenada) obj;

            if(this.latitud == null && this.longitud == null && cor2.latitud == null && cor2.longitud == null)
                return true;
            if(this.latitud != null && this.longitud != null && cor2.latitud == null && cor2.longitud == null)
                return false;
            if(this.latitud == null && this.longitud == null && cor2.latitud != null && cor2.longitud != null)
                return false;
            if(this.latitud == null && this.longitud != null && cor2.latitud == null && cor2.longitud != null)
                return false;
            if(this.latitud != null && this.longitud == null && cor2.latitud != null && cor2.longitud == null)
                return false;
            if(this.latitud != null && this.longitud == null && cor2.latitud == null && cor2.longitud != null)
                return false;
            if(this.latitud == null && this.longitud == null && cor2.latitud == null)
                return false;
            if(this.latitud == null && this.longitud == null)
                return false;
            if(this.latitud == null && cor2.latitud == null)
                return false;
            if(this.latitud != null && this.longitud == null && cor2.latitud == null)
                return false;
            if(Objects.equals (this.latitud, cor2.latitud) && Objects.equals (this.longitud, cor2.longitud))
                return true;

        }
        return false;
    }


}