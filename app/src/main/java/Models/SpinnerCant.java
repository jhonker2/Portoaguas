package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 15/05/2017.
 */

public class SpinnerCant {
    public Integer posicion,codigo,valor;

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public SpinnerCant(Integer posicion, Integer codigo, Integer valor) {
        this.posicion = posicion;
        this.codigo = codigo;
        this.valor = valor;
    }
}
