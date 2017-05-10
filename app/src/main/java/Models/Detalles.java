package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 17/04/2017.
 */

public class Detalles {
    public String cantidad,codigo,precio,cod_prod;


    public Detalles(String cantidad, String codigo, String precio, String cod_prod) {
        this.cantidad   = cantidad;
        this.codigo     = codigo;
        this.precio     = precio;
        this.cod_prod   = cod_prod;
    }


    public String getCod_prod() {
        return cod_prod;
    }

    public void setCod_prod(String cod_prod) {
        this.cod_prod = cod_prod;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
