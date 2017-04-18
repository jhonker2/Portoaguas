package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 04/04/2017.
 */

public class Puntos {
    public  String Hemisferio,zona,estado;
    public double latitud,longitud;
    public long codigomedidor,codigo;

    public String getHemisferio() {
        return Hemisferio;
    }

    public void setHemisferio(String hemisferio) {
        Hemisferio = hemisferio;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public long getCodigomedidor() {
        return codigomedidor;
    }

    public void setCodigomedidor(long codigomedidor) {
        this.codigomedidor = codigomedidor;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Puntos(Long codigo,String hemisferio, String zona, String estado, double latitud, double longitud, long codigomedidor) {
        this.codigo = codigo;
        this.Hemisferio = hemisferio;
        this.zona = zona;
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.codigomedidor = codigomedidor;

    }
}
