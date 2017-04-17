package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 17/04/2017.
 */

public class Detalles {
    public String cantidad,codigo,precio;


    public Detalles(String cantidad, String codigo, String precio) {
        this.cantidad = cantidad;
        this.codigo = codigo;
        this.precio = precio;
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
