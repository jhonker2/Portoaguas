package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 04/04/2017.
 */

public class Puntos {
    public String serie_medidor;
    public double latitud,longitud,deuda_portoagua;
    public long codigomedidor,codigo,id_tramite,numero_cuenta,codigo_cliente,mes_deuda,codigo_medidor;


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

    public long getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(long id_tramite) {
        this.id_tramite = id_tramite;
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

    public long getMes_deuda() {
        return mes_deuda;
    }

    public void setMes_deuda(long mes_deuda) {
        this.mes_deuda = mes_deuda;
    }

    public long getCodigo_medidor() {
        return codigo_medidor;
    }

    public void setCodigo_medidor(long codigo_medidor) {
        this.codigo_medidor = codigo_medidor;
    }

    public Puntos(String serie_medidor, double latitud, double longitud, double deuda_portoagua, long codigomedidor, long id_tramite, long numero_cuenta, long codigo_cliente, long mes_deuda, long codigo_medidor) {
        this.serie_medidor = serie_medidor;
        this.latitud = latitud;
        this.longitud = longitud;
        this.deuda_portoagua = deuda_portoagua;
        this.codigomedidor = codigomedidor;
        this.id_tramite = id_tramite;
        this.numero_cuenta = numero_cuenta;
        this.codigo_cliente = codigo_cliente;
        this.mes_deuda = mes_deuda;
        this.codigo_medidor = codigo_medidor;
    }
}
