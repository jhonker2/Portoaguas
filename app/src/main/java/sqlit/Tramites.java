package sqlit;

import android.content.ContentValues;

/**
 * Created by PMAT-PROGRAMADOR_1 on 12/05/2017.
 */

public class Tramites {
    public Long id_tramite,id_tarea_tramite,numero_cuenta,cod_cliente,cod_predio,mes_deuda;
    public Double latitud,longitud;
    public Float deuda_portoaguas;
    public String cod_medidor, serie_medidor;

    public Long getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(Long id_tramite) {
        this.id_tramite = id_tramite;
    }

    public Long getId_tarea_tramite() {
        return id_tarea_tramite;
    }

    public void setId_tarea_tramite(Long id_tarea_tramite) {
        this.id_tarea_tramite = id_tarea_tramite;
    }

    public Long getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(Long numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public Long getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cliente(Long cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public Long getCod_predio() {
        return cod_predio;
    }

    public void setCod_predio(Long cod_predio) {
        this.cod_predio = cod_predio;
    }

    public Long getMes_deuda() {
        return mes_deuda;
    }

    public void setMes_deuda(Long mes_deuda) {
        this.mes_deuda = mes_deuda;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Float getDeuda_portoaguas() {
        return deuda_portoaguas;
    }

    public void setDeuda_portoaguas(Float deuda_portoaguas) {
        this.deuda_portoaguas = deuda_portoaguas;
    }

    public String getCod_medidor() {
        return cod_medidor;
    }

    public void setCod_medidor(String cod_medidor) {
        this.cod_medidor = cod_medidor;
    }

    public String getSerie_medidor() {
        return serie_medidor;
    }

    public void setSerie_medidor(String serie_medidor) {
        this.serie_medidor = serie_medidor;
    }


    public Tramites(Long id_tramite, Long id_tarea_tramite, Long numero_cuenta, Long cod_cliente, Long cod_predio, Long mes_deuda, Double latitud, Double longitud, Float deuda_portoaguas, String cod_medidor, String serie_medidor) {
        this.id_tramite = id_tramite;
        this.id_tarea_tramite = id_tarea_tramite;
        this.numero_cuenta = numero_cuenta;
        this.cod_cliente = cod_cliente;
        this.cod_predio = cod_predio;
        this.mes_deuda = mes_deuda;
        this.latitud = latitud;
        this.longitud = longitud;
        this.deuda_portoaguas = deuda_portoaguas;
        this.cod_medidor = cod_medidor;
        this.serie_medidor = serie_medidor;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TramiteContrac.TramitesEntry.ID_TRAMITE,id_tramite);
        values.put(TramiteContrac.TramitesEntry.ID_TAREA_TRAMITE,id_tarea_tramite);
        values.put(TramiteContrac.TramitesEntry.NUMERO_CUENTA,numero_cuenta);
        values.put(TramiteContrac.TramitesEntry.LATITUD,latitud);
        values.put(TramiteContrac.TramitesEntry.LONGITUD,longitud);
        values.put(TramiteContrac.TramitesEntry.COD_CLIENTE,cod_cliente);
        values.put(TramiteContrac.TramitesEntry.COD_PREDIO,cod_predio);
        values.put(TramiteContrac.TramitesEntry.DEUDA_PORTOAGUAS,deuda_portoaguas);
        values.put(TramiteContrac.TramitesEntry.MES_DEUDA,mes_deuda);
        values.put(TramiteContrac.TramitesEntry.COD_MEDIDOR,cod_medidor);
        values.put(TramiteContrac.TramitesEntry.SERIE_MEDIDOR,serie_medidor);
        return values;
    }
}
