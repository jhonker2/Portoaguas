package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 19/07/2017.
 */

public class Clientes {
    public String cuenta,cedula,cliente_,direcion;
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCliente_() {
        return cliente_;
    }

    public void setCliente_(String cliente_) {
        this.cliente_ = cliente_;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public Clientes(String cuenta, String cedula, String cliente_, String direcion, Long id) {
        this.cuenta = cuenta;
        this.cedula = cedula;
        this.cliente_ = cliente_;
        this.direcion = direcion;
        this.id = id;
    }
}
