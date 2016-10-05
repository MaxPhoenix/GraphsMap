package gui;

import java.io.Serializable;

/**
 * Created by Agus on 30/9/2016.
 */
class Coordenada implements Serializable {
	private static  final long serialVersionUID = 1L;
    Double latitud, longitud;

    public Coordenada(){

    }
    public Coordenada(double lat, double lon) {
        this.latitud = lat;
        this.longitud = lon;
    }

    public Double getLat() {
        return this.latitud;
    }

    public Double getLon() {
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
            if(this.latitud == null && this.longitud == null && cor2.latitud == null && cor2.longitud != null)
                return false;
            if(this.latitud == null && this.longitud == null && cor2.latitud != null && cor2.longitud == null)
                return false;
            if(this.latitud == null && this.longitud != null && cor2.latitud == null && cor2.longitud == null)
                return false;
            if(this.latitud != null && this.longitud == null && cor2.latitud == null && cor2.longitud == null)
                return false;
            if(this.latitud == cor2.latitud && this.longitud == cor2.longitud )
                return true;

        }
        return false;
    }


}