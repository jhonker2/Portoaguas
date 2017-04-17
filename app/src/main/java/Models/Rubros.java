package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 13/04/2017.
 */

public class Rubros {

    private String codigo;
    private String titulos;
    private String precio;

    public Rubros(String codigo,String titulos, String precio) {
        this.codigo = codigo;
        this.titulos = titulos;
        this.precio = precio;
    }

    public Rubros(String codigo,String precio){
        this.codigo = codigo;
        this.precio = precio;

    }

    public String getTitulos() {
        return titulos;
    }

    public void setTitulos(String titulos) {
        this.titulos = titulos;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}


