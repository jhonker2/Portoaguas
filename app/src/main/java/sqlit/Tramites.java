package sqlit;

import android.content.ContentValues;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/05/2017.
 */

public class Tramites {
    public long id_tramite,id_tarea_tramite,numero_cuenta,codigo_cliente,codigo_predio,mes_deuda;
    public double latitud,longitud,deuda_portoagua;
    public String codigo_medidor,serie_medidor;


    public String getSerie_medidor() {
        return serie_medidor;
    }

    public void setSerie_medidor(String serie_medidor) {
        this.serie_medidor = serie_medidor;
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

    public double getDeuda_portoagua() {
        return deuda_portoagua;
    }

    public void setDeuda_portoagua(double deuda_portoagua) {
        this.deuda_portoagua = deuda_portoagua;
    }

    public long getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(long id_tramite) {
        this.id_tramite = id_tramite;
    }

    public long getId_tarea_tramite() {
        return id_tarea_tramite;
    }

    public void setId_tarea_tramite(long id_tarea_tramite) {
        this.id_tarea_tramite = id_tarea_tramite;
    }

    public long getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(long numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public long getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(long codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    public long getCodigo_predio() {
        return codigo_predio;
    }

    public void setCodigo_predio(long codigo_predio) {
        this.codigo_predio = codigo_predio;
    }

    public long getMes_deuda() {
        return mes_deuda;
    }

    public void setMes_deuda(long mes_deuda) {
        this.mes_deuda = mes_deuda;
    }

    public String getCodigo_medidor() {
        return codigo_medidor;
    }

    public void setCodigo_medidor(String codigo_medidor) {
        this.codigo_medidor = codigo_medidor;
    }

    public Tramites(long id_tramite, long id_tarea_tramite, long numero_cuenta, long codigo_cliente, long codigo_predio, long mes_deuda, double latitud, double longitud, double deuda_portoagua, String codigo_medidor, String serie_medidor) {
        this.id_tramite = id_tramite;
        this.id_tarea_tramite = id_tarea_tramite;
        this.numero_cuenta = numero_cuenta;
        this.codigo_cliente = codigo_cliente;
        this.codigo_predio = codigo_predio;
        this.mes_deuda = mes_deuda;
        this.latitud = latitud;
        this.longitud = longitud;
        this.deuda_portoagua = deuda_portoagua;
        this.codigo_medidor = codigo_medidor;
        this.serie_medidor = serie_medidor;
    }
}
