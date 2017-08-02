package Models;

/**
 * Created by PMAT-PROGRAMADOR_1 on 19/07/2017.
 */

public class Clientes {
    public String cuenta,cedula,cliente_,direcion, empresa;
    private boolean seleccionado=false;

    public String codigo_cliente, numero_cuenta, codigo_predio, telefono, correo, tipo_consumo, ciudadela, tipo_conexion, serie_medidor, numero_convenio,
    saldo_convenio,deuda_portoaguas,facturas_vencidas, deuda_crm, deuda_total, reclamo;


    public Clientes(String codigo_cliente, String numero_cuenta, String codigo_predio, String telefono, String correo,String cliente, String tipo_consumo,String cedula,String deuda_portoaguas, String facturas_vencidas) {
        this.cliente_ = cliente;
        this.cedula = cedula;
        this.codigo_cliente = codigo_cliente;
        this.numero_cuenta = numero_cuenta;
        this.codigo_predio = codigo_predio;
        this.telefono = telefono;
        this.correo = correo;
        this.tipo_consumo = tipo_consumo;
        this.deuda_portoaguas = deuda_portoaguas;
        this.facturas_vencidas = facturas_vencidas;
    /*    this.ciudadela = ciudadela;
        this.tipo_conexion = tipo_conexion;
        this.serie_medidor = serie_medidor;
        this.numero_convenio = numero_convenio;
        this.saldo_convenio = saldo_convenio;

        this.deuda_crm = deuda_crm;
        this.deuda_total = deuda_total;
        this.reclamo = reclamo;*/
    }

    public String getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(String codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getCodigo_predio() {
        return codigo_predio;
    }

    public void setCodigo_predio(String codigo_predio) {
        this.codigo_predio = codigo_predio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo_consumo() {
        return tipo_consumo;
    }

    public void setTipo_consumo(String tipo_consumo) {
        this.tipo_consumo = tipo_consumo;
    }

    public String getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(String ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getTipo_conexion() {
        return tipo_conexion;
    }

    public void setTipo_conexion(String tipo_conexion) {
        this.tipo_conexion = tipo_conexion;
    }

    public String getSerie_medidor() {
        return serie_medidor;
    }

    public void setSerie_medidor(String serie_medidor) {
        this.serie_medidor = serie_medidor;
    }

    public String getNumero_convenio() {
        return numero_convenio;
    }

    public void setNumero_convenio(String numero_convenio) {
        this.numero_convenio = numero_convenio;
    }

    public String getSaldo_convenio() {
        return saldo_convenio;
    }

    public void setSaldo_convenio(String saldo_convenio) {
        this.saldo_convenio = saldo_convenio;
    }

    public String getDeuda_portoaguas() {
        return deuda_portoaguas;
    }

    public void setDeuda_portoaguas(String deuda_portoaguas) {
        this.deuda_portoaguas = deuda_portoaguas;
    }

    public String getFacturas_vencidas() {
        return facturas_vencidas;
    }

    public void setFacturas_vencidas(String facturas_vencidas) {
        this.facturas_vencidas = facturas_vencidas;
    }

    public String getDeuda_crm() {
        return deuda_crm;
    }

    public void setDeuda_crm(String deuda_crm) {
        this.deuda_crm = deuda_crm;
    }

    public String getDeuda_total() {
        return deuda_total;
    }

    public void setDeuda_total(String deuda_total) {
        this.deuda_total = deuda_total;
    }

    public String getReclamo() {
        return reclamo;
    }

    public void setReclamo(String reclamo) {
        this.reclamo = reclamo;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
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

    public Clientes(String cuenta, String cedula, String cliente_, String direcion, String empresa) {
        this.cuenta = cuenta;
        this.cedula = cedula;
        this.cliente_ = cliente_;
        this.direcion = direcion;
        this.empresa = empresa;
    }
}
